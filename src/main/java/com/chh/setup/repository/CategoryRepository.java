package com.chh.setup.repository;

import com.chh.setup.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chh
 * @date 2020/3/14 20:31
 */
public interface CategoryRepository extends JpaRepository<CategoryModel, Integer> {
}
