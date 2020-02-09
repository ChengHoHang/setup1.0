package com.chh.setup.repository;

import com.chh.setup.entity.ArticleFavorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleFavorRepository extends JpaRepository<ArticleFavorEntity, Integer> {

    ArticleFavorEntity findByArticleIdAndUserId(Integer articleId, Integer userId);
}
