package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// TODO
@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    Optional<UserAuth> findUserAuthByTypeAndId(@Param("type") UserAuthType type, @Param("id") String id);

//    @AutoPwEncrypt
//    int insertUserAuth( UserAuth userAuth );
//
//    UserAuth findUserAuthByTypeAndId( UserAuth userAuth );
//
//    int deleteUserAuthByUserSeq(@Param("userSeq") Integer userSeq);
}
