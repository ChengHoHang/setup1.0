package com.chh.setup.repository;

import com.chh.setup.dto.ArticleDto;
import com.chh.setup.entity.ArticleFavorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleFavorRepository extends JpaRepository<ArticleFavorEntity, Integer> {

    ArticleFavorEntity findByArticleIdAndUserId(Integer articleId, Integer userId);

    @Query("select new com.chh.setup.dto.ArticleDto(u, a) from ArticleFavorEntity c " +
            "join ArticleEntity a on c.articleId = a.id " +
            "join UserEntity u on a.creator = u.id " +
            "and c.userId = :userId and c.state = 1")
    Page<ArticleDto> findAllByUserId(Integer userId, Pageable pageable);

    Long countByUserIdAndState(Integer userId, Integer state);
}
