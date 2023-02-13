package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long>, SuggestionRepositorySupport {
}
