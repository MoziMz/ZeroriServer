package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.BadWord;
import com.mozi.moziserver.repository.BadWordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadWordService {
    private static LinkedHashSet<String> badwords = new LinkedHashSet<>();
    private static Trie badwordsTrie;
    private final BadWordRepository badWordRepository;

    @PostConstruct
    public void addBadWord(){
        List<BadWord> badWordList=badWordRepository.findAll();

        for (BadWord badword: badWordList){
            badwords.add(badword.getContent());
        }

        badwordsTrie=Trie.builder().ignoreCase().addKeywords(badwords).build();
    }

    public Boolean isBadWord(String word) {
        Collection<Emit> emits = badwordsTrie.parseText(word);
        if (emits.size() != 0){
            return true;
        }
        return false;
    }

    public void createBadword(String content){
        final BadWord badWord=BadWord.builder()
                .content(content)
                .build();

        try {
            badWordRepository.save(badWord);

        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException();
        }
    }

}
