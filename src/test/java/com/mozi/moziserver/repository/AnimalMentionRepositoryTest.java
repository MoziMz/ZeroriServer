package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.AnimalMention;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnimalMentionRepositoryTest {

    @Autowired
    private AnimalMentionRepository animalMentionRepository;

    @Test
    @DisplayName("AnimalMention 적절하게 조회되는지 확인")
    void findAnimalMention() {
        AnimalMention animalMention = animalMentionRepository.findByAnimalSeqAndItemTurnAndPoint(2L, 2, 5);

        Assertions.assertEquals(2L, animalMention.getAnimal().getSeq());
        Assertions.assertEquals(2, animalMention.getItemTurn());
        Assertions.assertEquals("밤에도 낮에도 선명한 제로리를 보고싶어!", animalMention.getContent());
    }
}
