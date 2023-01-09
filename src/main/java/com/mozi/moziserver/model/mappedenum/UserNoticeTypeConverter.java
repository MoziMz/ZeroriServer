package com.mozi.moziserver.model.mappedenum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
