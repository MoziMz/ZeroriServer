package com.mozi.moziserver.model.mappedenum;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserNoticeTypeConverter implements AttributeConverter<UserNoticeType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserNoticeType userNoticeType) {
            return userNoticeType.getType();
    }

    @Override
    public UserNoticeType convertToEntityAttribute(Integer userNoticeTypeValue) {
            return UserNoticeType.valueOf(userNoticeTypeValue);
    }
}
