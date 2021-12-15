package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.model.req.ReqProfileUpdate;
import com.mozi.moziserver.repository.MyPageRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mozi.moziserver.common.Constant.EMAIL_DOMAIN_GROUPS;
import static com.mozi.moziserver.common.Constant.EMAIL_REGEX;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final MyPageRepository myPageRepository;
    private final EmailAuthService emailAuthService;
    private final PasswordEncoder passwordEncoder;

    // 닉네임, 이메일, 비밀번호 가져오기
    public UserAuth getUserInfo(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        return myPageRepository.getUserInfo(user);
    }

    // 닉네임 중복체크
    public User getUserNickName(Long userSeq, String nickName) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        return myPageRepository.getUserNickName(user, nickName);
    }

    // 이메일 중복체크
    public User getUserEmail(Long userSeq, String email) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        return myPageRepository.getUserEmail(user, email);
    }

    // 이메일 인증
    public void authUserEmail(Long userSeq, String email) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        if (!email.matches(EMAIL_REGEX)){
            throw ResponseError.BadRequest.INVALID_EMAIL.getResponseException();
        }

        int atIndex = email.lastIndexOf('@');
        String emailIdWithAt = email.substring(0, atIndex+1);
        String emailDomain = email.substring(atIndex+1).toLowerCase();
        List<String> currentDomainGroup = null;

        for(List<String> domainGroup : EMAIL_DOMAIN_GROUPS) {
            if(domainGroup.contains(emailDomain)) {
                currentDomainGroup = domainGroup;
                break;
            }
        }

        if(currentDomainGroup == null) {
            throw ResponseError.BadRequest.INVALID_EMAIL.getResponseException();
        }

        emailAuthService.sendCheckEmail(user, email);
    }

    // 닉네임, 비밀번호 수정
    public void updateUserInfo(Long userSeq, ReqProfileUpdate req) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        myPageRepository.updateUserInfo(
                user,
                req.getNickName(),
                passwordEncoder.encode(req.getPw()));
    }
}
