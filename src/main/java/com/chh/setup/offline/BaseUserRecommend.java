package com.chh.setup.offline;

import com.chh.setup.dto.req.Rating;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author chh
 * @date 2020/4/8 15:29
 */
@Component
public class BaseUserRecommend implements Serializable {

    @Autowired
    private transient DataLoader dataLoader;

    @Scheduled(cron = "0 0 1 * * ?")
    public void trainAndPredict() throws IOException {
        List<Rating> rawData = dataLoader.getRawData();

        //etl-wordcount流程
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

        ALS als = new ALS().setMaxIter(10).setRank(5).setRegParam(0.01)
                .setUserCol("userId").setItemCol("articleId").setRatingCol("score");

        ALSModel alsModel = als.fit(ratingDS);

        Dataset<Row> userRecs = alsModel.recommendForAllUsers(10);

        userRecs.foreachPartition(new ForeachPartitionFunction<Row>() {

            @Override
            public void call(Iterator<Row> t) throws Exception {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/surfing?" +
                                "useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true" +
                                "&useJDBCCompliantTimezoneShift=true" +
                                "&useLegacyDatetimeCode=false&serverTimezone=UTC",
                        "root", "123456");
                PreparedStatement statement = connection
                        .prepareStatement("insert into recommend(userId, recommendIds) values(?,?) ON DUPLICATE KEY UPDATE recommendIds = ?");

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
                        statement.setString(3, (String) stringObjectMap.get("recommendIds"));
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
    
    
}
