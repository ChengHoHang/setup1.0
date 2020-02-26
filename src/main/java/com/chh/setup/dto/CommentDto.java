package com.chh.setup.dto;

import com.chh.setup.dto.process.PageSuperDto;
import com.chh.setup.entity.CommentEntity;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.myutils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

/**
 * @author chh
 * @date 2020/2/5 0:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommentDto extends PageSuperDto {
    
    private Integer id;
    private String content;
    private Integer likeCount;
    private String gmtCreated;
    private UserDto commentator;
    private Integer articleId;
    private String articleTitle;
    private Integer favorState = 0;   //当前session用户是否喜欢该评论；若未登录默认为0

    public CommentDto(CommentEntity commentEntity, Integer articleId, String articleTitle) {
        BeanUtils.copyProperties(commentEntity, this);
        this.setGmtCreated(DateUtils.timestamp2Date(commentEntity.getGmtCreated(), "yyyy-MM-dd HH:mm"));
        this.articleId = articleId;
        this.articleTitle = articleTitle;
    }
    
    public CommentDto(CommentEntity commentEntity, UserEntity userEntity) {
        BeanUtils.copyProperties(commentEntity, this);
        this.setGmtCreated(DateUtils.timestamp2Date(commentEntity.getGmtCreated(), "yyyy-MM-dd HH:mm"));
        this.commentator = new UserDto(userEntity);
    }
}
