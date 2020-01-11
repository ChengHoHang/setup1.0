package com.chh.setup.dto;

import com.chh.setup.entity.UserEntity;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 将article与user表关联
 * 并用于后端返回至前端展示
 * @author chh
 * @date 2020/1/11 16:53
 */
@Data
public class ArticleDto {
    
    private Integer id;
    private String title;
    private String description;
    private Integer type;
    private String tag;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String gmtCreated;
    private String gmtModified;
    private UserEntity user;

    public static void showOnHome(ArticleDto articleDto) {
        articleDto.description = StringUtils.truncate(articleDto.description, 150) + ".....";
    }
}
