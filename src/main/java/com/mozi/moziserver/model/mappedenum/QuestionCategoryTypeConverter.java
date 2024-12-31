package com.mozi.moziserver.model.mappedenum;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class QuestionCategoryTypeConverter implements AttributeConverter<QuestionCategoryType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(QuestionCategoryType questionCategoryType) {
        return questionCategoryType.getType();
    }

    @Override
    public QuestionCategoryType convertToEntityAttribute(Integer questionCategoryTypeValue) {
        return QuestionCategoryType.valueOf(questionCategoryTypeValue);
    }
}