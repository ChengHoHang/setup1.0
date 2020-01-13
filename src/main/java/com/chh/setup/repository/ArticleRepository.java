package com.chh.setup.repository;

import com.chh.setup.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author chh
 * @date 2020/1/10 20:37
 */
public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {
    
    @Query("select e from ArticleEntity e where e.type = :type")
    Page<ArticleEntity> findAllByType(@Param("type") Integer type, Pageable pageable);
    
    Long countByType(Integer type);
}
