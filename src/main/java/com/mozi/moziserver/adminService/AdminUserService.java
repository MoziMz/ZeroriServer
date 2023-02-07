package com.mozi.moziserver.adminService;

import com.mozi.moziserver.common.UserState;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.adminReq.ReqAdminSignIn;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.repository.UserAuthRepository;
import com.mozi.moziserver.repository.UserRepository;
import com.mozi.moziserver.security.ResUserSignIn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final AuthenticationManager authenticationManager;

    public User getById(Long seq) {
        return userRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);
    }

    public Authentication signIn(ReqAdminSignIn req) {

        Authentication auth = null;

        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getId(), req.getPw())
            );
        } catch (Exception e) {
            throw ResponseError.BadRequest.INVALID_ID_OR_PASSWORD.getResponseException();
        }

        if (auth == null || !auth.isAuthenticated()) {
            throw ResponseError.BadRequest.BAD_REQUEST.getResponseException();
        }

        if (auth.getPrincipal() instanceof ResUserSignIn) {
            User user = userRepository.findById(((ResUserSignIn) auth.getPrincipal()).getUserSeq())
                    .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

            if (user.getState() == UserState.DELETED) {
                throw ResponseError.BadRequest.USER_IS_DELETED.getResponseException();
            }
        }

        return auth;
    }

    public List<UserAuth> getUserAuthList(
            String keyword,
            UserAuthType userAuthType,
            UserState userState,
            Integer pageNumber,
            Integer pageSize
    ) {
        return userAuthRepository.findAllByKeywordAndTypeAndState(keyword, userAuthType, userState, pageNumber, pageSize);
    }
}
