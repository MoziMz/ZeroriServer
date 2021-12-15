package com.mozi.moziserver.service;

import com.mozi.moziserver.model.entity.CurrentThemeList;
import com.mozi.moziserver.repository.CurrentThemeListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrentThemeListService {
    private final CurrentThemeListRepository currentThemeListRepository;

    public List<CurrentThemeList> getCurrentThemeList() {
        return currentThemeListRepository.findAllByOrderByTurn();
    }
}
