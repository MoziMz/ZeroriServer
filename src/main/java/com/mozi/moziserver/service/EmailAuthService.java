package com.mozi.moziserver.service;

import com.mozi.moziserver.common.JpaUtil;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.EmailAuth;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.EmailAuthResultState;
import com.mozi.moziserver.model.mappedenum.EmailAuthType;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.model.mappedenum.UserRoleType;
import com.mozi.moziserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static com.mozi.moziserver.common.Constant.PW_REGEX;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final PlatformTransactionManager transactionManager;
    private final JavaMailSender emailSender;
    private final EmailAuthRepository emailAuthRepository;
    private final UserAuthRepository userAuthRepository;
    private final UserRepository userRepository;
    private final UserRewardService userRewardService;

    private final PostboxMessageAnimalService postboxMessageAnimalService;

    private final AnimalRepository animalRepository;

    private final UserIslandRepository userIslandRepository;

    private final IslandService islandService;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String emailAddress;

    @Value("${server.domain}")
    private String serverDomain;

    /**
     * 가입 이메일 전송
     *
     * @param userAuth
     */
    public void sendJoinEmail(UserAuth userAuth) {
        if (userAuth.getType() != UserAuthType.EMAIL) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("UserAuth seq = " + userAuth.getSeq());
        }

        EmailAuth emailAuth = new EmailAuth();
        emailAuth.setType(EmailAuthType.JOIN);
        emailAuth.setId(userAuth.getId());
        emailAuth.setPw(userAuth.getPw());
        emailAuth.setUser(userAuth.getUser());

        saveEmailAuth(emailAuth);

        boolean isSend = sendEmail(
                emailAuth.getId(),
                "[제로리] 가입 인증 메일",
                "<html> <body><h3>안녕하세요. 제로리입니다.</h3>" + "아래 [인증] 버튼을 눌러주세요." + "<form action=\"" + serverDomain + "/email/auth/" + emailAuth.getToken() + "\"> <input type=\"submit\" value=\"인증\" /> </form>"
                        + "</body></html>"
        );

        if (!isSend) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    /**
     * 패스워드 변경 이메일 전송
     *
     * @param userAuth
     */
    public void sendResetPwEmail(UserAuth userAuth) {
        if (userAuth.getType() != UserAuthType.EMAIL) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        String tempPassword = getTempPassword();

        userAuth.setPw(passwordEncoder.encode(tempPassword));

        try {
            userAuthRepository.save(userAuth);
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        EmailAuth emailAuth = new EmailAuth();
        emailAuth.setType(EmailAuthType.RESET_PW);
        emailAuth.setId(userAuth.getId());
        emailAuth.setPw(userAuth.getPw());
        emailAuth.setUser(userAuth.getUser());

        saveEmailAuth(emailAuth);


        String content = "<h1>안녕하세요. 제로리입니다.<br/> 회원님의 임시 비밀번호는 " + tempPassword + " 입니다.<br/>" + "로그인 후에 비밀번호를 변경을 해주세요.</h1>";

        boolean isSend = sendEmail(
                emailAuth.getId(),
                "[제로리] 비밀번호 재설정 메일",
                content
        );

        if (!isSend) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    public String getTempPassword() {
        UUID uuidPassword = UUID.randomUUID();

        String tempPassword = uuidPassword.toString().substring(0, 8) + "!";

        if (!tempPassword.matches(PW_REGEX)) {
            throw ResponseError.BadRequest.INVALID_PASSWORD.getResponseException();
        }

        return tempPassword;
    }

    /**
     * 이메일 변경 이메일 전송
     *
     * @param userAuth
     * @param newEmail
     */
    public void sendResetEmail(UserAuth userAuth, String newEmail) {
        if (userAuth.getType() != UserAuthType.EMAIL) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        EmailAuth emailAuth = new EmailAuth();
        emailAuth.setType(EmailAuthType.RESET_EMAIL);
        emailAuth.setId(newEmail);
        emailAuth.setPw(userAuth.getPw());
        emailAuth.setUser(userAuth.getUser());

        saveEmailAuth(emailAuth);

        boolean isSend = sendEmail(
                newEmail,
                "[제로리] 이메일 변경 인증 메일",
                "<html> <body><h3>안녕하세요. 제로리입니다.</h3>" + "아래 [인증] 버튼을 눌러주세요." + "<form action=\"" + serverDomain + "/email/auth/" + emailAuth.getToken() + "\"> <input type=\"submit\" value=\"인증\" /> </form>"
                        + "</body></html>"
        );

        if (!isSend) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    /**
     * 이메일 인증
     *
     * @param token
     * @return
     */
    public EmailAuth authEmail(String token) {
        EmailAuth emailAuth = emailAuthRepository.findByToken(token)
                .orElseThrow(ResponseError.NotFound.EMAIL_AUTH_NOT_EXISTS::getResponseException);

        if (emailAuth.getUsedDt() != null) {

            createEmailAuth(emailAuth, EmailAuthResultState.ALREADY_SUCCESS);
            return emailAuth;

        }

        Duration duration = Duration.between(emailAuth.getCreatedAt(), LocalDateTime.now());
        boolean isExpired = duration.getSeconds() > emailAuth.getType().getExpiredSeconds();

        if (isExpired) {

            createEmailAuth(emailAuth, EmailAuthResultState.EXPIRATION);
            return emailAuth;

        }

        switch (emailAuth.getType()) {
            case JOIN:
                authJoin(emailAuth);
                break;
            case RESET_PW: // 로그인X, 앱 로그인 -> 비밀번호 재설정
                authResetPw(emailAuth);
                break;
            case RESET_EMAIL: // 로그인O, 이메일 바뀌어 있으면 됨
                authResetEmail(emailAuth);
                break;
        }

        createEmailAuth(emailAuth, EmailAuthResultState.SUCCESS);

        return emailAuth;
    }

    private void saveEmailAuth(EmailAuth emailAuth) {
        for (int retry = 0; retry < 3; retry++) {
            try {
                emailAuth.setToken(
                        UUID.randomUUID().toString().replaceAll("-", "")
                                + UUID.randomUUID().toString().replaceAll("-", "")
                );
                emailAuthRepository.save(emailAuth);
                if (emailAuth.getSeq() != null) {
                    break;
                }
            } catch (DataIntegrityViolationException e) {
                if (JpaUtil.isDuplicateKeyException(e)) {
                    continue;
                }
                throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
            } catch (Exception e) {
                throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
            }
        }

        if (emailAuth.getSeq() == null) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    /**
     * 이메일 전송
     *
     * @param to      받는 이메일
     * @param title   제목
     * @param content 내용
     * @return 메일 전송 요청 성공 여부
     */

    private boolean sendEmail(String to, String title, String content) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setSubject(title);
            messageHelper.setTo(to);
            messageHelper.setFrom("제로리 " + "<" + emailAddress + ">");
            messageHelper.setText(content, true);
            emailSender.send(message);
        } catch (Exception e) {
            throw ResponseError.BadRequest.BAD_REQUEST.getResponseException();
        }

        return true;
    }
//    private boolean sendEmail(String to, String title, String content) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(emailAddress);
//        message.setTo(to);
//        message.setSubject(title);
//        message.setText(content);
//        emailSender.send(message); // TODO handling throws MailException
//
//        return true;
//    }

    private void authJoin(EmailAuth emailAuth) {
        UserAuth userAuth = new UserAuth();
        userAuth.setType(UserAuthType.EMAIL);
        userAuth.setId(emailAuth.getId());
        userAuth.setPw(emailAuth.getPw());
        userAuth.setUser(emailAuth.getUser());
        userAuth.setRoleList(Arrays.asList(UserRoleType.ROLE_USER));

        try {
            withTransaction(() -> {
                userAuthRepository.save(userAuth);

                User user = userAuth.getUser();
                user.setEmail(userAuth.getId());
                userRepository.save(user);

                islandService.firstCreateUserIsland(user);
                userRewardService.firstCreateUserReward(user);
            });
        } catch (DataIntegrityViolationException e) {
            if (JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_EMAIL.getResponseException();
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_EXISTS_EMAIL.getResponseException();
        }

        if (userAuth.getSeq() == null) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    private void authResetPw(EmailAuth emailAuth) {

        UserAuth userAuth = userAuthRepository.findByUserAndType(emailAuth.getUser(), UserAuthType.EMAIL);
        if (userAuth == null) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        // TODO 로그인 상태로 만들기

        try {
            userAuthRepository.save(userAuth);
        } catch (DataIntegrityViolationException e) {
            if (JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_EMAIL.getResponseException();
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_EXISTS_EMAIL.getResponseException();
        }

        if (userAuth.getSeq() == null) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }


    private void authResetEmail(EmailAuth emailAuth) {

        UserAuth userAuth = userAuthRepository.findByUserAndType(emailAuth.getUser(), UserAuthType.EMAIL);
        if (userAuth == null) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        userAuth.setId(emailAuth.getId());

        try {
            userAuthRepository.save(userAuth);

            User user = userAuth.getUser();
            user.setEmail(userAuth.getId());
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_EMAIL.getResponseException();
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_EXISTS_EMAIL.getResponseException();
        }

        if (userAuth.getSeq() == null) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    public void createEmailAuth(EmailAuth emailAuth, EmailAuthResultState status) {

        if (status == EmailAuthResultState.SUCCESS) {
            emailAuth.setUsedDt(LocalDateTime.now());
        }
        emailAuth.setEmailAuthResultState(status);
        emailAuthRepository.save(emailAuth);

    }

    public void checkEmailAuth(User user) {

        EmailAuth emailAuth = emailAuthRepository.findByUserAndTypeOrderByCreatedAt(user, EmailAuthType.JOIN.getType())
                .orElseThrow(ResponseError.NotFound.EMAIL_AUTH_NOT_EXISTS::getResponseException);

        if (emailAuth.getEmailAuthResultState() == null) {
            throw ResponseError.BadRequest.INVALID_EMAIL_AUTH.getResponseException();
        } else if (emailAuth.getEmailAuthResultState() == EmailAuthResultState.EXPIRATION) {
            throw ResponseError.BadRequest.EXPIRED_EMAIL_AUTH.getResponseException();
        }

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
