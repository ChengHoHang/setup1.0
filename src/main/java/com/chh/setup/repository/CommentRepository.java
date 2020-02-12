package com.chh.setup.repository;

import com.chh.setup.dto.CommentDto;
import com.chh.setup.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    @Query("select new com.chh.setup.dto.CommentDto(c, u) " +
            "from CommentEntity c join UserEntity u on c.commentator = u.id and c.articleId = :id")
    List<CommentDto> getDtoByArticleId(Integer id, Sort sort);

    @Modifying
    @Query("update CommentEntity e set e.likeCount = e.likeCount + :count where e.id = :id")
    void incLikeCount(@Param("id") Integer id, @Param("count") int count);

    @Query("select new com.chh.setup.dto.CommentDto(c, a.id, a.title) " +
            "from CommentEntity c join ArticleEntity a on c.articleId = a.id and c.commentator = :commentator")
    Page<CommentDto> findAllByCommentator(@Param("commentator") Integer commentator, Pageable pageable);

    Long countByCommentator(Integer commentator);
}
