package com.mozi.moziserver.model.mappedenum;

import org.springframework.util.StringUtils;

import jakarta.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleTypeConverter implements AttributeConverter<List<UserRoleType>, String> {

    @Override
    public String convertToDatabaseColumn(List<UserRoleType> userRoleTypeList) {

        if (userRoleTypeList == null) {
            return null;
        }

        return userRoleTypeList.stream().map(UserRoleType::toString).collect(Collectors.joining(","));
    }

    @Override
    public List<UserRoleType> convertToEntityAttribute(String userRoleTypeListString) {

        if (!StringUtils.hasLength(userRoleTypeListString)) {
            return null;
        }

        return Arrays.stream(userRoleTypeListString.split(","))
                .map(UserRoleType::valueOf)
                .collect(Collectors.toList());
    }
}
