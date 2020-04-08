package com.chh.setup.offline;

import com.chh.setup.dto.req.Rating;
import com.chh.setup.enums.EventTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chh
 * @date 2020/4/7 19:06
 */
@Component
public class DataLoader {

    @Qualifier("highLevelClient")
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    
    public List<Rating> getRawData() throws IOException {
        SearchRequest searchRequest = new SearchRequest("record-2020");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.size(10000);
        searchRequest.source(sourceBuilder);

        List<Rating> rawData = new ArrayList<>();
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            int userId = (int) hit.getSourceAsMap().get("userId");
            int articleId = (int) hit.getSourceAsMap().get("articleId");
            String eventType = hit.getSourceAsMap().get("eventType").toString();
            int score;
            if (StringUtils.equals(eventType, EventTypeEnum.click.toString())) {
                score = 1;
            } else if (StringUtils.equals(eventType, EventTypeEnum.read.toString())) {
                score = 3;
            } else if (StringUtils.equals(eventType, EventTypeEnum.favor.toString())) {
                score = 10;
            } else {
                continue;
            }
            Rating rating = new Rating(userId, articleId, score);
            rawData.add(rating);
        }
        return rawData;
    }
}
