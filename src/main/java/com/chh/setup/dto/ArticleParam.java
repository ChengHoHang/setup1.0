package com.chh.setup.dto;

import lombok.Data;

/**
 * @author chh
 * @date 2020/1/10 20:44
 */
@Data
public class ArticleParam {
    
    private Integer id;
    private String type;
    private String title;
    private String description;
    private String tag;
    private Integer creator;
}
