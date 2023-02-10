package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.ConfirmReport;
import com.mozi.moziserver.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ConfirmReportRepository extends JpaRepository<ConfirmReport, Long> {
    ConfirmReport findByConfirmAndUser(Confirm confirm, User user);

    List<ConfirmReport> findByUser(User user);
}
