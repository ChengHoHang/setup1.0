package com.chh.setup.repository;


import com.chh.setup.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    
    UserModel findByAccountAndPassword(String account, String password);

    UserModel findByToken(String token);

//    @Query("select e from UserEntity e where e.id in (:userIds)")
//    List<UserEntity> findAllByIds(@Param("userIds") List<Integer> userIds);
}
