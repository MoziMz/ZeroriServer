package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// TODO
@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long>, UserAuthRepositorySupport {

    @Override
    <S extends UserAuth> S save(S entity);

    Optional<UserAuth> findUserAuthByTypeAndId(@Param("type") UserAuthType type, @Param("id") String id);

    UserAuth findByUserAndType(User user, UserAuthType type);
}
