package com.chh.setup.repository;

import com.chh.setup.dto.ArticleDto;
import com.chh.setup.entity.ArticleEntity;
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
public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {

    Long countByType(Integer type);

    Long countByCreator(Integer creator);

    @Modifying
    @Query("update ArticleEntity e set e.viewCount = e.viewCount + :count where e.id = :id")
    void incViewCount(@Param("id") Integer id, @Param("count") int count);

    @Modifying
    @Query("update ArticleEntity e set e.commentCount = e.commentCount + :count where e.id = :id")
    void incCommentCount(@Param("id") Integer id, @Param("count") int count);

    @Modifying
    @Query("update ArticleEntity e set e.likeCount = e.likeCount + :count where e.id = :id")
    void incLikeCount(@Param("id") Integer id, @Param("count") int count);

    @Query("select new com.chh.setup.dto.ArticleDto(a, u) " +
            "from ArticleEntity a join UserEntity u on a.creator = u.id and a.type = :type")
    Page<ArticleDto> getAllDtoByType(@Param("type") Integer type, Pageable pageable);

    @Query("select new com.chh.setup.dto.ArticleDto(a, u) " +
            "from ArticleEntity a join UserEntity u on a.creator = u.id")
    Page<ArticleDto> getAllDto(Pageable pageable);

    @Query("select new com.chh.setup.dto.ArticleDto(a, u) " +
            "from ArticleEntity a join UserEntity u on a.creator = u.id and a.id = :id")
    ArticleDto findDtoById(@Param("id") Integer id);

    @Query("select a from ArticleEntity a where a.creator = :creator")
    Page<ArticleEntity> findAllByCreator(@Param("creator") Integer creator, Pageable pageable);

    @Query(value = "select a.id, a.title from article a where a.tag REGEXP CONCAT('', :regexpTag,'') " +
            "and a.id != :id ORDER BY (likeCount + commentCount + 0.01 * viewCount) desc " +
            "limit :start, :size", nativeQuery = true)
    List<Object[]> getRelatedArticleById(@Param("id") Integer id, @Param("regexpTag") String regexpTag,
                                         @Param("start") Integer start, @Param("size") Integer size);
}
