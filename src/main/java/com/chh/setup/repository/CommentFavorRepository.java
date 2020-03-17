package com.chh.setup.repository;

import com.chh.setup.model.CommentFavorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentFavorRepository extends JpaRepository<CommentFavorModel, Integer> {
    
    @Query("select e.commentId from CommentFavorModel e " +
            "where e.commentId in (:commentIds) and e.userId = :userId and e.state = 1")
    List<Integer> getFavourComments(@Param("commentIds") List<Integer> commentIds,
                                               @Param("userId") Integer userId);
}
