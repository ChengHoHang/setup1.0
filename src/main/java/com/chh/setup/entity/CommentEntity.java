package com.chh.setup.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author chh
 * @date 2020/2/4 18:58
 */
@Data
@Entity
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer articleId;
    private Integer commentator;
    private String content;
    private Integer likeCount = 0;
    private Long gmtCreated;
    private Long gmtModified;
}
