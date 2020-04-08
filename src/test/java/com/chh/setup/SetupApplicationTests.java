package com.chh.setup;

import com.chh.setup.dto.req.Rating;
import com.chh.setup.offline.DataLoader;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@SpringBootTest
class SetupApplicationTests implements Serializable {

//    @Autowired
//    ArticleDao articleDao;
//
//    @Autowired
//    UserDao userDao;
//
//    @Autowired
//    CommentFavorDao commentFavorDao;
//
//    @Autowired
//    CommentDao commentDao;
//
//    @Autowired
//    NoticeDao noticeDao;
//
//    @Autowired
//    private FavorService favorService;

    @Autowired
    private transient DataLoader dataLoader;


    /**
     * Windows下Spark-mlib保存模型到本地报空指针解决方案
     * <p>
     * 出这种错误真是一脸懵逼，代码肯定是没问题的。
     * 二、解决办法
     * <p>
     * 下载编译好的winutils.exe，该文章里讲述了出错的原因；
     * 把该执行文件放置在某个路径下，如 c:\\winutils\\bin；
     * 在代码中加入这句话：System.setProperty("hadoop.home.dir","C:\\winutils")
     * 三、原因
     * 在上述引用的文章中，大致讲了一个意思：在Windows操作系统中，Hadoop中初始发行版本中的bug，导致Hadoop中用于评估文件权限的功能不能正常运行。在执行作业的时候，存储到HDFS的路径依旧是可达的，（上述错误虽然存在，但依旧在相应路径下保存了spark mlib跑出来的模型，只不过是没有数据），但是map/reduce作业会执行失败。
     * 这个异常是属于Hadoop在Windows上的bug所致，在Linux下不会报错，也不用加上那句话。
     * <p>
     * 由于项目需要不需要保存模型，我就不下载这东西了
     *
     * @throws IOException
     */
    @Test
    public void testETL() throws IOException, SQLException {
        List<Rating> rawData = dataLoader.getRawData();

        SparkSession spark = SparkSession.builder().master("local").appName("article-app").getOrCreate();
        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());
        JavaRDD<Rating> rawRatings = sc.parallelize(rawData);

        JavaPairRDD<String, Integer> ratingsRdd = rawRatings.mapToPair(new PairFunction<Rating, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(Rating rating) throws Exception {
                String recordKey = rating.getUserId() + "_" + rating.getArticleId();
                return new Tuple2<>(recordKey, rating.getScore());
            }
        });

        JavaPairRDD<String, Integer> countRdd = ratingsRdd.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });

        JavaRDD<Rating> resultRdd = countRdd.map(new Function<Tuple2<String, Integer>, Rating>() {
            @Override
            public Rating call(Tuple2<String, Integer> v1) throws Exception {
                String[] rawKey = v1._1.split("_");
                Integer userId = Integer.parseInt(rawKey[0]);
                Integer articleId = Integer.parseInt(rawKey[1]);
                return new Rating(userId, articleId, v1._2);
            }
        });

        Dataset<Row> ratingDS = spark.createDataFrame(resultRdd, Rating.class);
        Dataset<Row>[] splits = ratingDS.randomSplit(new double[]{0.8, 0.2});
        Dataset<Row> trainingData = splits[0];
        Dataset<Row> testingData = splits[1];

        ALS als = new ALS().setMaxIter(10).setRank(5).setRegParam(0.01)
                .setUserCol("userId").setItemCol("articleId").setRatingCol("score");

        /**
         * 使用ALSModel进行预测时，测试数据集中的用户和/或项目在训练模型期间不存在是很常见的。这通常发生在两种情况下：
         *
         * 在生产中，对于没有评级历史记录且未进行模型训练的新用户或物品（这是“冷启动问题”）。
         *
         * 在交叉验证过程中，数据分为训练集和评估集。当Spark中的使用简单随机拆分为CrossValidator或者TrainValidationSplit，它实际上是非常普遍遇到的评估集不是在训练集中的用户和/或项目。
         *
         * 默认情况，Spark在ALSModel.transform用户和/或项目因素不存在于模型中时分配NaN预测。这在生产系统中可能是有用的，因为它表名一个新的用户或项目，因此系统可以作为预测的一个后备决定。
         *
         * 然而，这在交叉验证期间是不希望的，因为任何NaN预测值都将影响NaN评估度量的结果（例如，在使用时RegressionEvaluator）。这使得模型选择变得不可能。Spark允许用户将coldStartStrategy参数设置为“drop”，以便删除DataFrame包含NaN值的预测中的任何行。
         *
         * 注意：目前支持的冷启动策略是“nan”（上面提到的默认行为）和“drop”。未来可能会支持进一步的策略。
         */
        ALSModel alsModel = als.fit(trainingData);
        alsModel.setColdStartStrategy("drop");
        Dataset<Row> predictions = alsModel.transform(testingData);

        RegressionEvaluator evaluator = new RegressionEvaluator()
                .setMetricName("rmse").setLabelCol("score").setPredictionCol("prediction");
        double rmse = evaluator.evaluate(predictions);
        System.out.println("rmse=" + rmse);
//        alsModel.save("file:///C:/Users/Administrator/Desktop/alsmodel");

        //随机
        Dataset<Row> userRecs = alsModel.recommendForAllUsers(10);
        userRecs.foreachPartition(new ForeachPartitionFunction<Row>() {
            
            @Override
            public void call(Iterator<Row> t) throws Exception {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/surfing?" +
                                "useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true" +
                                "&useJDBCCompliantTimezoneShift=true" +
                                "&useLegacyDatetimeCode=false&serverTimezone=UTC",
                        "root", "123456");
                PreparedStatement statement = connection.prepareStatement("insert into recommend(userId, recommendIds) values(?,?)");

                List<Map<String, Object>> sqlValueBatch = new ArrayList<>();
                
                t.forEachRemaining(data -> {
                    int userId = data.getInt(0);
                    List<GenericRowWithSchema> recommendList = data.getList(1);
                    List<Integer> articleIdList = new ArrayList<>();
                    recommendList.forEach(row -> {
                        int articleId = row.getInt(0);
                        articleIdList.add(articleId);
                    });
                    String recommendIds = StringUtils.join(articleIdList, ",");
                    Map<String, Object> sqlValue = new HashMap<>();
                    sqlValue.put("userId", userId);
                    sqlValue.put("recommendIds", recommendIds);
                    sqlValueBatch.add(sqlValue);
                });
                
                sqlValueBatch.forEach(stringObjectMap -> {
                    try {
                        statement.setInt(1, (Integer) stringObjectMap.get("userId"));
                        statement.setString(2, (String) stringObjectMap.get("recommendIds"));
                        statement.addBatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                statement.executeBatch();
                connection.close();
                
            }
        });

    }

//    @Test
//    void contextLoads() {
//        Sort sort = Sort.by(Sort.Direction.DESC, "gmtModified");
//        PageRequest pageRequest = PageRequest.of(2, 2, sort);
//        Page<ArticleEntity> pages = articleRepository.findAll(pageRequest);
//        List<ArticleEntity> content = pages.getContent();
//        for (ArticleEntity articleEntity : content) {
//            System.out.println(articleEntity.getTitle());
//        }
//    }
//
//    @Test
//    public void test1() {
////        Sort sort = Sort.by(Sort.Direction.DESC, "gmtModified");
////        PageRequest pageRequest = PageRequest.of(0, 5, sort);
////        List<ArticleEntity> contents = articleRepository.findAllByType(3, pageRequest).getContent();
////        for (ArticleEntity content : contents) {
////            System.out.println(content.getTitle());
////        }
//    }
//
//    @Test
//    public void test2() {
////        int page = 0;
////        int size = 5;
////        int type = 2;
////        Sort sort = Sort.by(Sort.Direction.DESC, "gmtModified");
////        PageRequest pageRequest = PageRequest.of(page, size, sort);
////        List<ArticleEntity> contents;
////        if (type == 0) {
////            contents = articleRepository.findAll(pageRequest).getContent();
////        } else {
////            contents = articleRepository.findAllByType(type, pageRequest).getContent();
////        }
////        for (ArticleEntity content : contents) {
////            System.out.println(content.getTitle());
////        }
//    }
//
//    @Test
//    public void test3() {
//        long count = articleRepository.countByType(3);
//        System.out.println(count);
//    }
//
//    @Test
//    public void test4() {
//        String name = ArticleTypeEnum.getName(1);
//        System.out.println(name);
//    }
//
//    @Test
//    public void test5() {
//        Integer type = 1;
//        CommentFavorEntity favor = new CommentFavorEntity();
//        favor.setUserId(4);
//        commentFavorRepository.save(favor);
//    }
//
//    @Test
//    public void test6() {
//        CommentFavorEntity favor = commentFavorRepository.findById(1).get();
//        System.out.println(favor);
//    }
//
//    @Test
//    public void test7() {
////        List<CommentFavorEntity> record = commentFavorRepository.getFavourComments(Arrays.asList(2, 3), 4, 35);
////        System.out.println(record);
//    }
//
//    @Test
//    public void test8() {
//        ArrayList<Object> list = new ArrayList<>();
//        test9(list);
//        System.out.println(list);
//    }
//
//    public void test9(ArrayList<Object> list) {
//        list.add(1);
//    }
//
//    @Test
//    public void test9() {
//        UserEntity userEntity2 = new UserEntity();
//        userEntity2.setAccount("ab123456");
//        userEntity2.setId(7);
//        userEntity2.setPassWord("123456");
//        List<UserEntity> list = new ArrayList<>();
//        list.add(userEntity2);
//        userRepository.saveAll(list);
//    }
//
//    @Test
//    public void test10() {
//        commentRepository.incLikeCount(2, 1);
//    }
//
//    @Test
//    public void test11() {
//        String test = "/u/12";
//        String s = "/u/4/sdfsdf";
//        System.out.println(Arrays.toString(test.split("/")));
//        System.out.println(Integer.parseInt(s.split("/")[3]));
//    }
//
//    @Test
//    public void test12() {
////        System.out.println(UserRecordTypeEnum.ARTICLE.toString().equals("article".toUpperCase()));
//    }
//
//    @Test
//    public void test13() {
////        List<ArticleDto> articles = articleRepository.findAllByArticleIds(4);
////        System.out.println(articles);
//    }
//    
//    @Test
//    public void test14() {
//        PageRequest pageRequest = PageUtils.getDefaultPageRequest(1, 5, "gmtModified");
//        Page<ArticleDto> articles = articleRepository.getAllDtoByType(3, pageRequest);
//        System.out.println(articles.getContent());
//    }
//    
//    @Test
//    public void test15() {
//        Map<String, String> map = MapUtils.mergeMaps(GeneralTag.getMap(), InternetTag.getMap(),
//                EntertainmentTag.getMap(), SportTag.getMap());
//        System.out.println(map);
//    }
//
//    @Test
//    public void test16() {
//        String[] tags = StringUtils.split("102,104", ",");
//        String[] collect = Arrays.stream(tags).map(x -> TagService.getIdMap().get(x)).toArray(String[]::new);
//        System.out.println(collect);
//    }
//
//    @Test
//    public void test17() {
//        String test = " ";
//        String replace = StringUtils.replace(test, ",", "");
//        System.out.println(StringUtils.isBlank(replace));
//    }
//
//    @Test
//    public void test18() {
//        List<Object[]> list = articleRepository.getRelatedArticleById(20, "101|104", 0, 15);
//        System.out.println(list);
//    }

//    @Test
//    public void test19() {
//        Sort sort = Sort.by(Sort.Order.asc("state"), Sort.Order.desc("gmtCreated"));
//        PageRequest pageRequest = PageRequest.of(0, 5, sort);
//        Page<NoticeDto> allByReceiver = noticeRepository.findAllByReceiver(5, pageRequest);
//        System.out.println(allByReceiver);
//    }

//    @Test
//    @Transactional
//    public void test20() {
//        NoticeModel noticeModel = new NoticeModel();
//        noticeModel.setNotifier(5);
//        noticeModel.setReceiver(4);
//        noticeModel.setType(1);
//        noticeModel.setParentId(48);
//        try {
//            noticeRepository.save(noticeModel);
//        } catch (DataIntegrityViolationException ex) {
//            ex.printStackTrace();
//        }
//    }


//    @Test
//    @Transactional
//    @Rollback(value = false)
//    public void test21() {
//        List<Record> records = new ArrayList<>();
//        Record e = new Record();
//        e.setCommentatorId(4);
//        e.setCommentId(38);
//        e.setState(1);
//        records.add(e);
//        records.add(e);
//        favorService.createOrUpdateStates(records, 48, 4);
//    }

//    @Test
//    public void test22() {
//        Record record1 = new Record();
//        Record record2 = new Record();
//        record1.setCommentatorId(1);
//        record2.setCommentatorId(1);
//        record1.setCommentId(1);
//        record2.setCommentId(1);
//        record1.setState(1);
//        record2.setState(1);
//        System.out.println(record1.equals(record2));
//        List<Record> objects = Arrays.asList(record1, record2);
//        List<Record> collect = objects.stream().distinct().collect(Collectors.toList());
//    }

}
