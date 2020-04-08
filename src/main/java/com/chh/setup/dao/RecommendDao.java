package com.chh.setup.dao;

import com.chh.setup.model.RecommendModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendDao extends JpaRepository<RecommendModel, Integer> {
    
}
