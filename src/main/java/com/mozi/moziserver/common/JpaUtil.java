package com.mozi.moziserver.common;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import java.util.Optional;

public class JpaUtil {
    private final static Integer SQL_ERROR_UNIQUE_KEY_DUPLICATE = 1062; // MySQL 한정 에러 코드

    public static boolean isDuplicateKeyException(DataIntegrityViolationException e) {
        if (e.getCause() == null || !(e.getCause() instanceof ConstraintViolationException)) {
            return false;
        }

        return Optional.of((ConstraintViolationException) e.getCause())
                .map(ConstraintViolationException::getSQLException)
                .map(SQLException::getErrorCode)
                .filter(SQL_ERROR_UNIQUE_KEY_DUPLICATE::equals)
                .isPresent();
    }
}
