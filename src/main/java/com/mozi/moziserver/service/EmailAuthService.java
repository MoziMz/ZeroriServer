package com.mozi.moziserver.service;

import com.mozi.moziserver.common.EmailAuthResult;
import com.mozi.moziserver.common.JpaUtil;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.EmailAuth;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.EmailAuthType;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.repository.EmailAuthRepository;
import com.mozi.moziserver.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final JavaMailSender emailSender;
    private final EmailAuthRepository emailAuthRepository;
    private final UserAuthRepository userAuthRepository;

    @Value("${spring.mail.username}")
    private String emailAddress;

    @Value("${server.domain}")
    private String serverDomain;

    public void sendJoinEmail(UserAuth userAuth) {
        EmailAuth emailAuth = new EmailAuth();
        emailAuth.setType(EmailAuthType.JOIN);
        emailAuth.setId(userAuth.getId());
        emailAuth.setPw(userAuth.getPw());
        emailAuth.setUser(userAuth.getUser());

        for(int retry=0;retry<3;retry++) {
            try {
                emailAuth.setToken(
                        UUID.randomUUID().toString().replaceAll("-","")
                        + UUID.randomUUID().toString().replaceAll("-","")
                );
                emailAuthRepository.save(emailAuth);
                if(emailAuth.getSeq() != null) {
                    break;
                }
            } catch (DataIntegrityViolationException e) {
                if(JpaUtil.isDuplicateKeyException(e)) {
                    continue;
                }
                throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
            } catch (Exception e) {
                throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
            }
        }

        if(emailAuth.getSeq() == null) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        boolean isSend = sendEmail(
                emailAuth.getId(),
                "가입인증메일",
                "인증링크 : " + serverDomain + "/email/auth/" + emailAuth.getToken()
        );

        if(!isSend) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    public EmailAuthResult authEmail(String token) {
        Optional<EmailAuth> emailAuthOptional = emailAuthRepository.findByToken(token);

        if(emailAuthOptional.isEmpty()) {
            return EmailAuthResult.INVALID;
        }

        EmailAuth emailAuth = emailAuthOptional.get();

        if(emailAuth.getType() != EmailAuthType.JOIN) {
            return EmailAuthResult.INVALID;
        }

        if(emailAuth.getUsedDt() != null) {
            return EmailAuthResult.ALREADY_SUCC;
        }

        Duration duration = Duration.between(emailAuth.getCreatedAt(), LocalDateTime.now());
        boolean isExpired = duration.getSeconds() > emailAuth.getType().getExpiredSeconds();

        if( isExpired ) {
            return EmailAuthResult.INVALID;
        }

        UserAuth userAuth = new UserAuth();
        userAuth.setType(UserAuthType.EMAIL);
        userAuth.setId(emailAuth.getId());
        userAuth.setPw(emailAuth.getPw());
        userAuth.setUser(emailAuth.getUser());

        try {
            userAuthRepository.save(userAuth);
        } catch (DataIntegrityViolationException e) { // TODO
            if(JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_EMAIL.getResponseException();
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_EXISTS_EMAIL.getResponseException();
        }

        if(userAuth.getSeq() == null) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        emailAuth.setUsedDt(LocalDateTime.now());
        emailAuthRepository.save(emailAuth);

        return EmailAuthResult.SUCC;
    }

    private boolean sendEmail(String to,String title,String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailAddress);
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        emailSender.send(message); // TODO handling throws MailException

        return true;
    }
}
