package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositorySupport{
}
