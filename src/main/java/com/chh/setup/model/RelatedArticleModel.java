package com.chh.setup.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author chh
 * @date 2020/4/5 17:43
 */
@Data
@Entity
@Table(name = "related_article")
public class RelatedArticleModel {

    @Id
    private Integer articleId;
    private String relatedIds;
}
