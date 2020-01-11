package com.chh.setup.dto;

import lombok.Data;

/**
 * 从前端获取的dto，在发布编辑页面提交时传输给后端
 * @author chh
 * @date 2020/1/10 20:44
 */
@Data
public class ArticleFEDto {
    
    private String type;
    private String title;
    private String description;
    private String tag;
    private Integer creator;
}
