package com.chh.setup.entity;

import com.chh.setup.entity.key.ArticleFavorKey;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @author chh
 * @date 2020/2/8 20:57
 */
@Data
@Entity
@Table(name = "article_favor")
@IdClass(ArticleFavorKey.class)
public class ArticleFavorEntity {

    @Id
    private Integer articleId;
    @Id
    private Integer userId;
    private Integer state;     // 点赞状态：0表示取消点赞，1表示已点赞
    private Long gmtModified;
}
