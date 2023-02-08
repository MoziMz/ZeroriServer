package com.mozi.moziserver.model.mappedenum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
