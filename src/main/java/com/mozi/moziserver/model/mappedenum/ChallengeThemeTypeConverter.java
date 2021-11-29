package com.mozi.moziserver.model.mappedenum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ChallengeThemeTypeConverter implements AttributeConverter<ChallengeThemeType, String> {
    @Override
    public String convertToDatabaseColumn(ChallengeThemeType challengeThemeType) {
        return challengeThemeType.getName();
    }

    @Override
    public ChallengeThemeType convertToEntityAttribute(String challengeThemesValue) {
        return ChallengeThemeType.valueOf(challengeThemesValue);
    }

}
