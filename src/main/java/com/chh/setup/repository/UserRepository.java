package com.chh.setup.repository;


import com.chh.setup.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    
    UserEntity findByAccount(String account);

    UserEntity findByToken(String token);

    @Query("select e from UserEntity e where e.id in (:userIds)")
    List<UserEntity> findAllByIds(@Param("userIds") List<Integer> userIds);
}
