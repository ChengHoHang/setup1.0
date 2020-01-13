package com.chh.setup.repository;


import com.chh.setup.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("select e from UserEntity e where e.account = :account ")
    UserEntity findByAccount(@Param("account") String account);

    @Query("select e from UserEntity e where e.token = :token")
    UserEntity findByToken(@Param("token") String token);
    
}
