package com.chh.setup.dao;

import com.chh.setup.model.RelatedArticleModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chh
 * @date 2020/4/5 17:58
 */
public interface RelatedArticleDao extends JpaRepository<RelatedArticleModel, Integer> {
    
}
