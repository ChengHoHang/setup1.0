package com.chh.setup.repository;

import com.chh.setup.model.ArticleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author chh
 * @date 2020/1/10 20:37
 */
public interface ArticleRepository extends JpaRepository<ArticleModel, Integer> {

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

    @Query(value = "select a.id, a.title from article a where a.tag REGEXP CONCAT('', :regexpTag,'') " +
            "and a.id != :id ORDER BY (likeCount + commentCount + 0.01 * viewCount) desc " +
            "limit :start, :size", nativeQuery = true)
    List<Object[]> getRelatedArticleById(@Param("id") Integer id, @Param("regexpTag") String regexpTag,
                                         @Param("start") Integer start, @Param("size") Integer size);
}
