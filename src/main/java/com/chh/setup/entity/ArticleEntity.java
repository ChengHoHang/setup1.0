package com.chh.setup.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author chh
 * @date 2020/1/10 20:33
 */
@Data
@Entity
@Table(name = "Article")
public class ArticleEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private Integer type;
    private String tag;
    private Integer creator;
    private Integer commentCount = 0;
    private Integer viewCount = 0;
    private Integer likeCount = 0;
    private Long gmtCreated;
    private Long gmtModified;
}
