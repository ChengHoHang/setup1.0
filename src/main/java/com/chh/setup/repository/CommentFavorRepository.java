package com.chh.setup.repository;

import com.chh.setup.entity.CommentFavorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentFavorRepository extends JpaRepository<CommentFavorEntity, Integer> {
    
    @Query("select e from CommentFavorEntity e " +
            "where e.commentId in (:commentIds) and e.userId = :userId and " +
            "e.articleId = :articleId and e.state = 1")
    List<CommentFavorEntity> getFavourComments(@Param("commentIds") List<Integer> commentIds,
                                               @Param("userId") Integer userId,
                                               @Param("articleId") Integer articleId);
}
