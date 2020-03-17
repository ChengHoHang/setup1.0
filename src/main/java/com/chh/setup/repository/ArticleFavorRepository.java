package com.chh.setup.repository;

import com.chh.setup.model.ArticleFavorModel;
import com.chh.setup.model.ArticleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleFavorRepository extends JpaRepository<ArticleFavorModel, Integer> {

    ArticleFavorModel findByArticleIdAndUserId(Integer articleId, Integer userId);

    @Query("select new com.chh.setup.model.ArticleModel(a, u) from ArticleFavorModel c " +
            "join ArticleModel a on c.articleId = a.id " +
            "join UserModel u on a.authorId = u.id " +
            "and c.userId = :userId and c.state = :state")
    Page<ArticleModel> findAllByUserIdAndState(Integer userId, Integer state, Pageable pageable);
    
    Long countByUserIdAndState(Integer userId, Integer state);
}
