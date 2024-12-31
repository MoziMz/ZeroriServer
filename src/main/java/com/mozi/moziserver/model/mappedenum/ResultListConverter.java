package com.mozi.moziserver.model.mappedenum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozi.moziserver.model.UserChallengeResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;

/**
 * Mysql post links 타입 JSON
 * JPA Post links 타입 List<Link>
 */
@Slf4j
@Converter
public class ResultListConverter implements AttributeConverter<List<UserChallengeResult>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(final List<UserChallengeResult> resultList) {

        if (resultList == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(resultList);
        } catch (JsonProcessingException e) {
            log.error("UserChallengeResultListConverter", e);
            return null;
        }
    }

    @Override
    public List<UserChallengeResult> convertToEntityAttribute(final String resultListString) {

        if (!StringUtils.hasLength(resultListString)) {
            return null;
        }

        try {
            return objectMapper.readValue(resultListString, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("UserChallengeResultListConverter", e);
            return null;
        }
    }
}