package com.chh.setup.model;

import com.chh.setup.model.key.CommentFavorKey;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author chh
 * @date 2020/2/5 19:05
 */
@Data
@Entity
@Table(name = "comment_favor")
@IdClass(CommentFavorKey.class)
public class CommentFavorModel implements Serializable {

    @Id
    private Integer commentId;
    @Id
    private Integer userId;
    private Integer articleId;
    private Integer state;     // 点赞状态：0表示取消点赞，1表示已点赞
    private Date updateTime;
}
