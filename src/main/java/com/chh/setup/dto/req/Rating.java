package com.chh.setup.dto.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chh
 * @date 2020/4/7 19:27
 */
@Data
public class Rating implements Serializable {

    private Integer userId;
    private Integer articleId;
    private Integer score;
    
    public Rating(Integer userId, Integer articleId, Integer score) {
        this.userId = userId;
        this.articleId = articleId;
        this.score = score;
    }
}
