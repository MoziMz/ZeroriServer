package com.mozi.moziserver.model.mappedenum;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserAuthTypeConverter implements AttributeConverter<UserAuthType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserAuthType userAuthType) {
        return userAuthType.getType();
    }

    @Override
    public UserAuthType convertToEntityAttribute(Integer userAuthTypeValue) {
        return UserAuthType.valueOf(userAuthTypeValue);
    }
}
