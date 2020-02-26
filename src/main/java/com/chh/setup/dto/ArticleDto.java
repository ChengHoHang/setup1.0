package com.chh.setup.dto;

import com.chh.setup.dto.process.PageSuperDto;
import com.chh.setup.entity.ArticleEntity;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.enums.ArticleTypeEnum;
import com.chh.setup.myutils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 将article与user表关联
 * 需要封装至PagesDto再由前端渲染
 *
 * @author chh
 * @date 2020/1/11 16:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleDto extends PageSuperDto {

    private Integer id;
    private String title;
    private String description;
    private String type;
    private String[] tags;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String gmtCreated;
    private String gmtModified;
    private UserDto creator;
    private List<CommentDto> comments;
    private Integer favorState = 0;   //当前session用户是否喜欢该文章；若未登录默认为0
    private List<Object[]> relatedArticle;

    public ArticleDto(ArticleEntity articleEntity) {
        BeanUtils.copyProperties(articleEntity, this);
        this.setGmtModified(DateUtils.timestamp2Date(articleEntity.getGmtModified(), "yyyy-MM-dd HH:mm"));
    }

    public ArticleDto(UserEntity userEntity, ArticleEntity articleEntity) {
        this(articleEntity);
        this.creator = new UserDto(userEntity);
    }

    public ArticleDto(ArticleEntity articleEntity, UserEntity userEntity) {
        this(articleEntity);
        this.setGmtCreated(DateUtils.timestamp2Date(articleEntity.getGmtCreated(), "yyyy-MM-dd HH:mm"));
        this.setType(ArticleTypeEnum.getName(articleEntity.getType()));
        this.creator = new UserDto(userEntity);
        this.tags = StringUtils.split(articleEntity.getTag(), "|");
    }
}
