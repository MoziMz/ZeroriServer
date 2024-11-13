package com.mozi.moziserver.service;

import com.mozi.moziserver.model.entity.ConfirmBlockHistory;
import com.mozi.moziserver.repository.ConfirmBlockHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConfirmBlockHistoryService {
    private final ConfirmBlockHistoryRepository confirmBlockHistoryRepository;

    public void saveBlockHistory(Long confirmSeq) {
        ConfirmBlockHistory confirmBlockHistory = ConfirmBlockHistory.builder()
                .confirmSeq(confirmSeq)
                .build();
        confirmBlockHistoryRepository.save(confirmBlockHistory);
    }
}
