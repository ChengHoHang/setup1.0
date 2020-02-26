package com.chh.setup.dto;

import lombok.Data;

/**
 * @author chh
 * @date 2020/2/4 18:49
 */
@Data
public class CommentParam {

    private Integer articleId;
    private Integer commentator;
    private String content;
    private Integer author;
}
