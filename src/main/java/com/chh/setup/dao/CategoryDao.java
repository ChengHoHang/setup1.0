package com.chh.setup.dao;

import com.chh.setup.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chh
 * @date 2020/3/14 20:31
 */
public interface CategoryDao extends JpaRepository<CategoryModel, Integer> {
}
