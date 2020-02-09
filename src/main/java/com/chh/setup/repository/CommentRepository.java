package com.chh.setup.repository;

import com.chh.setup.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    List<CommentEntity> findByArticleIdOrderByGmtModified(Integer id);

    @Modifying
    @Query("update CommentEntity e set e.likeCount = e.likeCount + :count where e.id = :id")
    void incLikeCount(@Param("id") Integer id, @Param("count") int count);
}
