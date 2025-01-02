package com.mozi.moziserver.model.mappedenum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozi.moziserver.model.ChallengeExplanation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Mysql post links 타입 JSON
 * JPA Post links 타입 List<Link>
 */
@Slf4j
@Converter
public class ExplanationConverter implements AttributeConverter<ChallengeExplanation, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(final ChallengeExplanation challengeExplanation) {

        if (challengeExplanation == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(challengeExplanation);
        } catch (JsonProcessingException e) {
            log.error("ChallengeExplanationConverter", e);
            return null;
        }
    }

    @Override
    public ChallengeExplanation convertToEntityAttribute(final String challengeExplanationString) {

        if (!StringUtils.hasLength(challengeExplanationString)) {
            return null;
        }

        try {
            return objectMapper.readValue(challengeExplanationString, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("ChallengeExplanationConverter", e);
            return null;
        }
    }
}