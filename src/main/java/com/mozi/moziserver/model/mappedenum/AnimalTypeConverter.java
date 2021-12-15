package com.mozi.moziserver.model.mappedenum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AnimalTypeConverter implements AttributeConverter<AnimalType, String> {
    @Override
    public String convertToDatabaseColumn(AnimalType animalType) {
        return animalType.getName();
    }

    @Override
    public AnimalType convertToEntityAttribute(String animalValue) {
        return AnimalType.valueOf(animalValue);
    }
}
