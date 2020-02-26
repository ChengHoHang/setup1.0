package com.chh.setup.dto;

import com.chh.setup.dto.process.PageSuperDto;
import com.chh.setup.entity.ArticleEntity;
import com.chh.setup.entity.CommentEntity;
import com.chh.setup.entity.NoticeEntity;
import com.chh.setup.entity.UserEntity;
import com.chh.setup.enums.NoticeTypeEnum;
import com.chh.setup.myutils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chh
 * @date 2020/2/25 10:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeDto extends PageSuperDto {

    private Integer id;
    private UserDto notifier;
    private String action;
    private Integer articleId;
    private String content;
    private Integer type;
    private String gmtCreated;
    private Integer state;

    public NoticeDto(NoticeEntity noticeEntity, ArticleEntity articleEntity, 
                     CommentEntity commentEntity, UserEntity userEntity) {
        this.notifier = new UserDto(userEntity);
        this.gmtCreated = DateUtils.timestamp2Date(noticeEntity.getGmtCreated(), "yyyy-MM-dd HH:mm");
        this.state = noticeEntity.getState();
        this.type = noticeEntity.getType();
        this.id = noticeEntity.getId();
        Integer type = noticeEntity.getType();
        this.action = NoticeTypeEnum.getAction(type);
        if (type == 1 || type == 2) {
            this.content = articleEntity.getTitle();
            this.articleId = articleEntity.getId();
        } else if (type == 3) {
            this.content = commentEntity.getContent();
            this.articleId = commentEntity.getArticleId();
        }
    }
}
