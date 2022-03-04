package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ConfirmRepository extends JpaRepository<Confirm, Long>, ConfirmRepositorySupport{
    @Modifying
    @Query(value = "DELETE FROM confirm WHERE seq = :confirmSeq", nativeQuery = true)
    int deleteConfirm(@Param("confirmSeq") Long confirmSeq);
}
