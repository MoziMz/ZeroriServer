package com.mozi.moziserver.service;

import com.mozi.moziserver.common.JpaUtil;
import com.mozi.moziserver.common.UserState;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.entity.UserFcm;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.model.req.ReqResign;
import com.mozi.moziserver.model.req.ReqUserSignIn;
import com.mozi.moziserver.model.req.ReqUserSignUp;
import com.mozi.moziserver.repository.AnimalRepository;
import com.mozi.moziserver.repository.UserAuthRepository;
import com.mozi.moziserver.repository.UserFcmRepository;
import com.mozi.moziserver.repository.UserRepository;
import com.mozi.moziserver.security.ReqUserSocialSignIn;
import com.mozi.moziserver.security.ResUserSignIn;
import com.mozi.moziserver.security.UserSocialAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Optional;

import static com.mozi.moziserver.common.Constant.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;
    private final UserFcmRepository userFcmRepository;
    private final AuthenticationManager authenticationManager;
    private final UserSocialAuthenticationProvider userSocialAuthenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final EmailAuthService emailAuthService;
    private final PlatformTransactionManager transactionManager;
    private final IslandService islandService;
    private final AnimalRepository animalRepository;
    private final PostboxMessageAnimalService postboxMessageAnimalService;
    private final UserRewardService userRewardService;
    private final BadWordService badWordService;

    public User signUp(ReqUserSignUp reqUserSignUp) {

        if (reqUserSignUp.getType() != UserAuthType.EMAIL) {
            throw ResponseError.BadRequest.INVALID_EMAIL.getResponseException();
        }

        String email = reqUserSignUp.getId();
        if (!isValidEmail(email)) {
            throw ResponseError.BadRequest.INVALID_EMAIL.getResponseException();
        }

        String emailId = getEmailId(email);
        List<String> currentDomainGroup = getCurrentEmailDomainGroup(email);
        if (emailId == null || currentDomainGroup == null) {
            throw ResponseError.BadRequest.INVALID_EMAIL.getResponseException();
        }

        if (!reqUserSignUp.getPw().matches(PW_REGEX)) {
            throw ResponseError.BadRequest.INVALID_PASSWORD.getResponseException();
        }

        for (String domain : currentDomainGroup) {
            boolean isExists = userAuthRepository.findUserAuthByTypeAndId(UserAuthType.EMAIL, emailId + "@" + domain).isPresent();
            if (isExists) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_EMAIL.getResponseException();
            }
        }

        UserAuth userAuth = new UserAuth();
        userAuth.setId(reqUserSignUp.getId());
        userAuth.setPw(passwordEncoder.encode(reqUserSignUp.getPw()));
        userAuth.setType(UserAuthType.EMAIL);

        User user = new User();
        userRepository.save(user);

        if (user.getSeq() == null) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        userAuth.setUser(user);

        emailAuthService.sendJoinEmail(userAuth);

        return user;
    }

    public Authentication signIn(ReqUserSignIn req) {

        Authentication auth = null;

        if (req.getType() == UserAuthType.EMAIL) {
            try {
                auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(req.getId(), req.getPw())
                );
            } catch (Exception e) {
                throw ResponseError.BadRequest.INVALID_ID_OR_PASSWORD.getResponseException();
            }
        } else {
            ReqUserSocialSignIn reqUserSocialSignIn = new ReqUserSocialSignIn(req);

            auth = authenticationManager.authenticate(reqUserSocialSignIn);

            // TODO checkQuitUser 바꿔야한다.

            if ((auth == null || !auth.isAuthenticated()) && req.getType().isSocial()) {
                if (req.getType() == UserAuthType.KAKAO) kakaoSignUp(req);
                else if (req.getType() == UserAuthType.APPLE) appleSignUp(req);
                else if(req.getType() == UserAuthType.NAVER) naverSignUp(req);
                else if(req.getType() == UserAuthType.GOOGLE) googleSignUp(req);

                auth = authenticationManager.authenticate(reqUserSocialSignIn);
            }
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

    private void kakaoSignUp(ReqUserSignIn reqUserSignIn) {
        final String accessToken = reqUserSignIn.getId();

        final String kakaoSocialId = userSocialAuthenticationProvider.getKakaoSocialId(accessToken);

        if (kakaoSocialId == null) {
            return;
        }

        createSocialUser(UserAuthType.KAKAO, kakaoSocialId);
    }

    private void appleSignUp(ReqUserSignIn reqUserSignIn) {
        final String identityToken = reqUserSignIn.getId();

        final String appleSocialId = userSocialAuthenticationProvider.getAppleSocialId(identityToken);

        if (appleSocialId == null) {
            return;
        }

        createSocialUser(UserAuthType.APPLE, appleSocialId);
    }

    private void naverSignUp(ReqUserSignIn reqUserSignIn) {
        final String accessToken = reqUserSignIn.getId();

        final String naverSocialId = userSocialAuthenticationProvider.getNaverSocialId(accessToken);

        if (naverSocialId == null) {
            return;
        }

        createSocialUser(UserAuthType.NAVER, naverSocialId);
    }

    private void googleSignUp(ReqUserSignIn reqUserSignIn) {
        final String accessToken = reqUserSignIn.getId();

        final String googleSocialId = userSocialAuthenticationProvider.getGoogleSocialId(accessToken);

        if (googleSocialId == null) {
            return;
        }

        createSocialUser(UserAuthType.GOOGLE, googleSocialId);
    }

    private void createSocialUser(UserAuthType userAuthType, String socialId) {
        withTransaction(() -> {

            User user = new User();
            userRepository.save(user);

            if (user.getSeq() == null) {
                return;
            }

            UserAuth userAuth = new UserAuth();
            userAuth.setType(userAuthType);
            userAuth.setId(socialId);
            userAuth.setUser(user);

            userAuthRepository.save(userAuth);

            // UserIsland 생성
            islandService.firstCreateUserIsland(user);

            //동물의 편지 생성
            Animal firstAnimal = animalRepository.findByIslandTypeAndIslandLevel(1,2);
            postboxMessageAnimalService.createPostboxMessageAnimal(user,firstAnimal);

            //UserReword 생성
            userRewardService.firstCreateUserReward(user);
        });
    }

    public Optional<UserAuth> findUserAuthByTypeAndId(UserAuthType type, String id) {
        return userAuthRepository.findUserAuthByTypeAndId(type, id);
    }

    public void updateNickname(User user, String nickname) {


        if (badWordService.isBadWord(nickname)){
            throw ResponseError.BadRequest.NICKNAME_WITH_BAD_WORD.getResponseException();
        }

        if (!nickname.matches(NICKNAME_REGEX)){
            throw ResponseError.BadRequest.INVALID_NICKNAME.getResponseException();
        }

        user.setNickName(nickname);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_NICKNAME.getResponseException();
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    public void updatePw(User user, String pw) {

        UserAuth userAuth = userAuthRepository.findByUserAndType(user, UserAuthType.EMAIL);
        if (userAuth == null) {
            throw ResponseError.BadRequest.SOCIAL_LOGIN_USER.getResponseException("social login user cannot change password");
        }

        //기존 비밀번호와 새 비밀번호가 같은지 확인
        checkPassword(userAuth.getPw(),pw);

        if (!pw.matches(PW_REGEX)) {
            throw ResponseError.BadRequest.INVALID_PASSWORD.getResponseException();
        }

        userAuth.setPw(passwordEncoder.encode(pw));

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    public void updateEmail(User user, String email) {
        UserAuth userAuth = userAuthRepository.findByUser(user);

        emailAuthService.sendResetEmailEmail(userAuth, email);
    }

    public UserAuth findUserAuthByNicknameAndPw(String nickName, String pw) {

        User user = userRepository.findByNickName(nickName);

        if (user == null) {
            throw ResponseError.NotFound.NICKNAME_NOT_EXISTS.getResponseException();
        }

        UserAuth userAuth = userAuthRepository.findByUser(user);

        if (!passwordEncoder.matches(pw, userAuth.getPw())) {
            throw ResponseError.NotFound.USER_NOT_EXISTS.getResponseException("password not matched");
        }

        return userAuth;
    }

    public boolean checkEmailDuplicate(String email) {

        if (!isValidEmail(email)) {
            throw ResponseError.BadRequest.INVALID_EMAIL.getResponseException();
        }

        String emailId = getEmailId(email);
        List<String> currentDomainGroup = getCurrentEmailDomainGroup(email);
        if (emailId == null || currentDomainGroup == null) {
            throw ResponseError.BadRequest.INVALID_EMAIL.getResponseException();
        }

        for (String domain : currentDomainGroup) {
            boolean isExists = userAuthRepository.findUserAuthByTypeAndId(UserAuthType.EMAIL, emailId + "@" + domain).isPresent();
            if (isExists) {
                return true;
            }
        }

        User user = userAuthRepository.findUserSeqByEmail(email);

        return !(user == null || user.getState() == UserState.DELETED);
    }

    public boolean checkNickNameDuplicate(String nickName) {

        if (badWordService.isBadWord(nickName)){
            throw ResponseError.BadRequest.NICKNAME_WITH_BAD_WORD.getResponseException();
        }

        return userRepository.existsByNickName(nickName);
    }

    public boolean checkPassword(String oldPw,String newPw) {

        if(passwordEncoder.matches(newPw,oldPw)) {
            throw ResponseError.BadRequest.MATCH_AN_EXISTING_PASSWORD.getResponseException();
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            return false;
        }

        List<String> currentDomainGroup = getCurrentEmailDomainGroup(email);

        return currentDomainGroup != null;
    }


    private List<String> getCurrentEmailDomainGroup(String email) {
        int atIndex = email.lastIndexOf('@');
        String emailDomain = email.substring(atIndex + 1).toLowerCase();

        for (List<String> domainGroup : EMAIL_DOMAIN_GROUPS) {
            if (domainGroup.contains(emailDomain)) {
                return domainGroup;
            }
        }

        return null;
    }

    private String getEmailId(String email) {
        int atIndex = email.lastIndexOf('@');
        if (atIndex < 0) {
            return null;
        }
        return email.substring(0, atIndex);
    }

    public Optional<User> getUserBySeq(Long userSeq) {
        return userRepository.findById(userSeq);
    }

    public void upsertUserFcm(User user, String deviceId, String token) {
        UserFcm userFcm = new UserFcm();
        userFcm.setDeviceId(deviceId);
        userFcm.setToken(token);
        userFcm.setUser(user);

        try {
            userFcmRepository.save(userFcm);
            return;
        } catch (DataIntegrityViolationException e) {
            if (!JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
            }
        }

        userFcm = userFcmRepository.findUserFcmByDeviceId(deviceId)
                .orElseThrow(ResponseError.InternalServerError.UNEXPECTED_ERROR::getResponseException);
        userFcm.setToken(token);
        userFcm.setUser(user);

        userFcmRepository.save(userFcm);
    }

    public void resignUser(User user, ReqResign req) {
        String reasonString = "";
        req.getResignReasonTypeList().stream().map(resignReasonType -> reasonString.concat(resignReasonType.getName()));
        user.setStateReason(reasonString);

        user.setState(UserState.DELETED);

        userRepository.save(user);
        // TODO DeleteLog에 삭제된 회원정보 넣기
    }

    private void withTransaction(Runnable runnable) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();

        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            runnable.run();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }
    }
}
