package com.chh.setup.dao;

import com.chh.setup.model.ArticleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author chh
 * @date 2020/1/10 20:37
 */
public interface ArticleDao extends JpaRepository<ArticleModel, Integer> {

    Long countByCategoryId(Integer categoryId);

    Long countByAuthorId(Integer authorId);

    @Modifying
    @Query("update ArticleModel a set a.viewCount = a.viewCount + :count where a.id = :id")
    void incViewCount(@Param("id") Integer id, @Param("count") int count);

    @Modifying
    @Query("update ArticleModel a set a.commentCount = a.commentCount + :count where a.id = :id")
    void incCommentCount(@Param("id") Integer id, @Param("count") int count);

    @Modifying
    @Query("update ArticleModel a set a.likeCount = a.likeCount + :count where a.id = :id")
    void incLikeCount(@Param("id") Integer id, @Param("count") int count);
    
    Page<ArticleModel> findAllByCategoryId(Integer categoryId, Pageable pageable);
    
    Page<ArticleModel> findAllByAuthorId(@Param("authorId") Integer authorId, Pageable pageable);

    @Query("select a.title from ArticleModel a where a.id = :id")
    String getTitle(@Param("id") Integer id);
}
