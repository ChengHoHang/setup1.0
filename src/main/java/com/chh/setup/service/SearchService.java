package com.chh.setup.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chh.setup.dto.res.SearchDto;
import com.chh.setup.model.ArticleModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chh
 * @date 2020/3/23 18:28
 */
@Service
public class SearchService {

    @Qualifier("highLevelClient")
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ArticleService articleService;

    public SearchDto searchByKeyword(String keyword, String categoryIds, Integer page) throws IOException {
        Request request = new Request("GET", "/article/_search");
        String requestJson = getReqJson(keyword, categoryIds, page);
        request.setJsonEntity(requestJson);
        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        String resStr = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(resStr);

        List<ArticleModel> articleModelList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            int articleId = Integer.parseInt(jsonObj.get("_id").toString());
            ArticleModel articleModel = articleService.getEntityById(articleId);
            JSONObject highlightObj = jsonObj.getJSONObject("highlight");
            if (highlightObj != null && highlightObj.containsKey("title")) {
                String title = highlightObj.getJSONArray("title").get(0).toString();
                articleModel.setTitle(title);
            }
            if (highlightObj != null && highlightObj.containsKey("description")) {
                String description = highlightObj.getJSONArray("description").get(0).toString() + "...";
                articleModel.setDescription(description);
            } else {
                articleModel.setDescription(StringUtils.truncate(articleModel.getDescription(), 150) + ".....");
            }
            articleModel.setTags(Arrays.stream(StringUtils.split(articleModel.getTag(), " ")).limit(2).toArray(String[]::new));
            articleModelList.add(articleModel);
        }

        SearchDto searchDto = new SearchDto();
        searchDto.setData(articleModelList);
        long totalCount = Long.parseLong(jsonObject.getJSONObject("hits").getJSONObject("total").get("value").toString());
        searchDto.setTotalPage((long) Math.ceil((double) totalCount / 10));
        searchDto.setTotalCount(totalCount);
        searchDto.setOtherKeyword(analyzeKeyword(keyword));
        return searchDto;
    }

    private String getReqJson(String keyword, String categoryIds, Integer page) {
        JSONObject jsonResObj = new JSONObject();

        jsonResObj.put("_source", "false");

        //*->query 构建query
        jsonResObj.put("query", new JSONObject());
        //*->query->function_score
        jsonResObj.getJSONObject("query").put("function_score", new JSONObject());


        //*->query->function_score->query
        jsonResObj.getJSONObject("query").getJSONObject("function_score").put("query", new JSONObject());
        jsonResObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").put("bool", new JSONObject());
        jsonResObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").put("must", new JSONArray());

        JSONObject firstObj = new JSONObject();
        firstObj.put("multi_match", new JSONObject());
        firstObj.getJSONObject("multi_match").put("query", keyword);
        firstObj.getJSONObject("multi_match").put("fields", new JSONArray(Arrays.asList("title^2", "description^1.5", "tag")));
        firstObj.getJSONObject("multi_match").put("type", "best_fields");
        firstObj.getJSONObject("multi_match").put("tie_breaker", 0.3);
        jsonResObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                .getJSONArray("must").add(firstObj);

        if (categoryIds != null) {
            JSONObject secondObj = new JSONObject();
            secondObj.put("terms", new JSONObject());
            secondObj.getJSONObject("terms").put("categoryId", new JSONArray(Arrays.asList(categoryIds.split(","))));
            jsonResObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool")
                    .getJSONArray("must").add(secondObj);
        }


        //*->query->function_score->functions
        jsonResObj.getJSONObject("query").getJSONObject("function_score").put("functions", new JSONArray());

        JSONObject expFuncObj = new JSONObject();
        expFuncObj.put("exp", new JSONObject());
        JSONObject exp_value_obj = new JSONObject();
        exp_value_obj.put("origin", "now");
        exp_value_obj.put("scale", "60d");
        exp_value_obj.put("offset", "30d");
        exp_value_obj.put("decay", 0.5);
        expFuncObj.getJSONObject("exp").put("createTime", exp_value_obj);
        expFuncObj.put("weight", 2);
        jsonResObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(expFuncObj);

        JSONObject likeCountScore = new JSONObject();
        likeCountScore.put("script_score", new JSONObject());
        likeCountScore.getJSONObject("script_score").put("script", new JSONObject());
        likeCountScore.getJSONObject("script_score").getJSONObject("script").put("source", "doc['likeCount'].value < 10 ? Math.log10(doc['likeCount'].value + 1) : 1");
        jsonResObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(likeCountScore);

        JSONObject commentCountScore = new JSONObject();
        commentCountScore.put("script_score", new JSONObject());
        commentCountScore.getJSONObject("script_score").put("script", new JSONObject());
        commentCountScore.getJSONObject("script_score").getJSONObject("script").put("source", "doc['commentCount'].value < 10 ? Math.log10(doc['commentCount'].value + 1) : 1");
        jsonResObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(commentCountScore);

        JSONObject viewCountScore = new JSONObject();
        viewCountScore.put("script_score", new JSONObject());
        viewCountScore.getJSONObject("script_score").put("script", new JSONObject());
        viewCountScore.getJSONObject("script_score").getJSONObject("script").put("source", "1.0 - 1.0 / (0.1 * doc['viewCount'].value + 1)");
        jsonResObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(viewCountScore);        

        
        //*->query->function_score->score_mode
        jsonResObj.getJSONObject("query").getJSONObject("function_score").put("score_mode", "sum");

        
        //*->query->function_score->boost_mode
        jsonResObj.getJSONObject("query").getJSONObject("function_score").put("boost_mode", "sum");
        
        
        //*->highlight 构建highlight
        jsonResObj.put("highlight", new JSONObject());
        //*->highlight->fields
        jsonResObj.getJSONObject("highlight").put("fields", new JSONObject());

        JSONObject highlight_description = new JSONObject();
        highlight_description.put("fragment_size", 200);
        highlight_description.put("number_of_fragments", 1);
        jsonResObj.getJSONObject("highlight").getJSONObject("fields").put("description", highlight_description);
        jsonResObj.getJSONObject("highlight").getJSONObject("fields").put("title", new JSONObject());
        

        //分页
        jsonResObj.put("from", 10 * (page - 1));
        jsonResObj.put("size", 10);

        return jsonResObj.toJSONString();
    }

    private List<String> analyzeKeyword(String keyword) throws IOException {
        Request request = new Request("GET", "/article/_analyze");
        String analyzeReq = getAnalyzeJson(keyword);
        request.setJsonEntity(analyzeReq);
        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        String resStr = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(resStr);

        JSONArray jsonTokens = jsonObject.getJSONArray("tokens");
        List<String> tokens = new ArrayList<>();

        for (int i = 0; i < jsonTokens.size(); i++) {
            String token = jsonTokens.getJSONObject(i).get("token").toString();
            if (!keyword.equals(token)) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    private String getAnalyzeJson(String keyword) {
        JSONObject jsonResObj = new JSONObject();
        jsonResObj.put("analyzer", "ik_max_word");
        jsonResObj.put("text", keyword);
        return jsonResObj.toJSONString();
    }

}
