package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.Declaration;
import com.mozi.moziserver.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {
    Declaration findByConfirmAndUser(Confirm confirm, User user);

    List<Declaration> findByUser(User user);

}
