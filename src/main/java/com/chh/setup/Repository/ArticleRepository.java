package com.chh.setup.Repository;

import com.chh.setup.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chh
 * @date 2020/1/10 20:37
 */
public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {
}
