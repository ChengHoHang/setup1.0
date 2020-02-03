package com.chh.setup.dto;

import com.chh.setup.entity.UserEntity;
import lombok.Data;

/**
 * 将article与user表关联
 * 需要封装至PagesDto再由前端渲染
 * @author chh
 * @date 2020/1/11 16:53
 */
@Data
public class ArticleDto {
    
    private Integer id;
    private String title;
    private String description;
    private String type;
    private String tag;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String gmtCreated;
    private String gmtModified;
    private UserEntity user;
    
}