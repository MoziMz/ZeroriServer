package com.mozi.moziserver.model.mappedenum;

import javax.persistence.Converter;
import javax.persistence.AttributeConverter;

@Converter(autoApply = true)
public class ChallengeTagTypeConverter implements AttributeConverter<ChallengeTagType, String>{
    @Override
    public String convertToDatabaseColumn(ChallengeTagType challengeTagType) {
        return challengeTagType.getName();
    }

    @Override
    public ChallengeTagType convertToEntityAttribute(String challengeTagsValue) {
        return ChallengeTagType.valueOf(challengeTagsValue);
    }

}
