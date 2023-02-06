package com.mozi.moziserver.model.mappedenum;

import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleTypeConverter implements AttributeConverter<List<UserRoleType>, String> {

    @Override
    public String convertToDatabaseColumn(List<UserRoleType> userRoleTypeList) {

        if (userRoleTypeList == null) {
            return null;
        }

        String userRoleTypeListString = "";
        for (UserRoleType userRoleType: userRoleTypeList) {
            userRoleTypeListString.concat(userRoleType.toString());
        }
        return userRoleTypeListString;
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
