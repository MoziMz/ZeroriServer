package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserIsland;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserIslandRepository extends JpaRepository<UserIsland, Long>, UserIslandRepositorySupport {
}
