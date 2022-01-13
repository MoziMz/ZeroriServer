package com.mozi.moziserver.model.mappedenum;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozi.moziserver.model.PlanDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

// Mysql post links 타입 JSON
// JPA Post links 타입 List<Link>

// AttributeConverter = Mysql <-> JPA 간 컬럼 데이터 변환 도구
// convertToDatabaseColumn = JPA(links List<Link>) -> Mysql(links JSON) = serialize
// convertToEntityAttribute = Mysql(links JSON) -> JPA(links List<Link>) = deserialize

@Slf4j
@Converter
public class PlanDateListConverter implements AttributeConverter<List<PlanDate>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(final List<PlanDate> planDateList) {

        if (planDateList == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(planDateList);
        } catch (JsonProcessingException e) {
            log.error("PlanDateConverter", e);
            return null;
        }
    }

    @Override
    public List<PlanDate> convertToEntityAttribute(final String planDateListString) {

        if (!StringUtils.hasLength(planDateListString)) {
            return null;
        }

        try {
            return objectMapper.readValue(planDateListString, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("UserChallengePlanDateConverter", e);
            return null;
        }
    }
}