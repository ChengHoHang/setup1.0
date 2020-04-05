package com.chh.setup.dao;

import com.chh.setup.model.CommentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentDao extends JpaRepository<CommentModel, Integer> {

    List<CommentModel> findAllByArticleId(Integer articleId, Sort sort);

    Page<CommentModel> findAllByCommentatorId(Integer commentatorId, Pageable pageable);

    Long countByCommentatorId(Integer commentatorId);

    @Modifying
    @Query("update CommentModel e set e.likeCount = e.likeCount + :count where e.id = :id")
    void incLikeCount(@Param("id") Integer id, @Param("count") int count);
}
