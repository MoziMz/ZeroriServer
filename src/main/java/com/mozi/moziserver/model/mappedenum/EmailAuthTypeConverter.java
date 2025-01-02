package com.mozi.moziserver.model.mappedenum;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmailAuthTypeConverter implements AttributeConverter<EmailAuthType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EmailAuthType emailAuthType) {
        return emailAuthType.getType();
    }

    @Override
    public EmailAuthType convertToEntityAttribute(Integer emailAuthTypeValue) {
        return EmailAuthType.valueOf(emailAuthTypeValue);
    }
}
