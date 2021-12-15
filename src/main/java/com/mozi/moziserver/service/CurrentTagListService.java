package com.mozi.moziserver.service;

import com.mozi.moziserver.model.entity.CurrentTagList;
import com.mozi.moziserver.repository.CurrentTagListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrentTagListService {
    private final CurrentTagListRepository currentTagListRepository;

    public List<CurrentTagList> getCurrentTagList() {
        return currentTagListRepository.findAllByOrderByTurn();
    }

}
