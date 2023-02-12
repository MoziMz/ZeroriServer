package com.mozi.moziserver.model.mappedenum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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