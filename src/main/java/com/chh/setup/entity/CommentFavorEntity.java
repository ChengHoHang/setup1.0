package com.chh.setup.entity;

import com.chh.setup.entity.key.CommentFavorKey;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author chh
 * @date 2020/2/5 19:05
 */
@Data
@Entity
@Table(name = "comment_favor")
@IdClass(CommentFavorKey.class)
public class CommentFavorEntity implements Serializable {

    @Id
    private Integer commentId;
    @Id
    private Integer userId;
    private Integer articleId;
    private Integer state;     // 点赞状态：0表示取消点赞，1表示已点赞
    private Long gmtModified;
}
