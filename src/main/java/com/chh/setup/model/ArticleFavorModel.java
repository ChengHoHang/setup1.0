package com.chh.setup.model;

import com.chh.setup.model.key.ArticleFavorKey;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author chh
 * @date 2020/2/8 20:57
 */
@Data
@Entity
@Table(name = "article_favor")
@IdClass(ArticleFavorKey.class)
public class ArticleFavorModel {

    @Id
    private Integer articleId;
    @Id
    private Integer userId;
    private Integer state;     // 点赞状态：0表示取消点赞，1表示已点赞
    private Date updateTime;
}
