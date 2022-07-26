package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositorySupport{
    User findByNickName(String nickName);

    boolean existsByNickName(String nickName);
}
