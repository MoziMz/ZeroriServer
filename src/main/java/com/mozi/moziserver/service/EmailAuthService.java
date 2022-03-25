package com.mozi.moziserver.service;

import com.amazonaws.services.s3.internal.eventstreaming.Message;
import com.mozi.moziserver.common.EmailAuthResult;
import com.mozi.moziserver.common.JpaUtil;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.EmailAuth;
import com.mozi.moziserver.model.entity.EmailCheckAuth;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.mappedenum.EmailAuthType;
import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
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
    private final EmailCheckAuthRepository emailCheckAuthRepository;
    private final MyPageRepository myPageRepository;
    private final UserRepository userRepository;

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

    public void sendCheckEmail(User user, String email) {
        EmailCheckAuth emailCheckAuth = new EmailCheckAuth();
        emailCheckAuth.setType(EmailAuthType.RESET_PW_OR_EMAIL);
        emailCheckAuth.setEmail(email);
        emailCheckAuth.setUser(user);

        for(int retry=0;retry<3;retry++) {
            try {
                emailCheckAuth.setToken(
                        UUID.randomUUID().toString().replaceAll("-","")
                                + UUID.randomUUID().toString().replaceAll("-","")
                );
                emailCheckAuthRepository.save(emailCheckAuth);
                if(emailCheckAuth.getSeq() != null) {
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

        if(emailCheckAuth.getSeq() == null) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        boolean isSend = sendButtonedEmail(
                email,
                "이메일 수정 인증메일",
                "<html> <body><h1></h1>"+"<br/>아래 [인증] 버튼을 눌러주세요."+"<form action=\""+serverDomain+ "/email/auth/other-check/" + emailCheckAuth.getToken()+"\"> <input type=\"submit\" value=\"인증\" /> </form>"
                        +"</body></html>"
        );

        if(!isSend) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    public EmailAuthResult authCheckEmail(Long userSeq, String token) {
        Optional<EmailCheckAuth> emailCheckAuthOptional = emailCheckAuthRepository.findByToken(token);

        if(emailCheckAuthOptional.isEmpty()) {
            return EmailAuthResult.INVALID;
        }

        EmailCheckAuth emailCheckAuth = emailCheckAuthOptional.get();

        if(emailCheckAuth.getType() != EmailAuthType.JOIN) {
            return EmailAuthResult.INVALID;
        }

        if(emailCheckAuth.getUsedDt() != null) {
            return EmailAuthResult.ALREADY_SUCC;
        }

        Duration duration = Duration.between(emailCheckAuth.getCreatedAt(), LocalDateTime.now());
        boolean isExpired = duration.getSeconds() > emailCheckAuth.getType().getExpiredSeconds();

        if( isExpired ) {
            return EmailAuthResult.INVALID;
        }

        try {
            User user = userRepository.findById(userSeq)
                    .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

            String email = emailCheckAuthRepository.getUserEmail(user).getEmail();

            myPageRepository.updateUserEmail(user, email);
        }
        catch (DataIntegrityViolationException e) { // TODO
            if(JpaUtil.isDuplicateKeyException(e)) {
                throw ResponseError.BadRequest.ALREADY_EXISTS_EMAIL.getResponseException();
            }
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_EXISTS_EMAIL.getResponseException();
        }

        emailCheckAuth.setUsedDt(LocalDateTime.now());
        emailCheckAuthRepository.save(emailCheckAuth);

        return EmailAuthResult.SUCC;
    }

    private boolean sendButtonedEmail(String to,String title,String content) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setSubject(title);
            messageHelper.setTo(to);
            messageHelper.setFrom(emailAddress);
            messageHelper.setText(content, true);
            emailSender.send(message);
        }catch(Exception e){
            throw ResponseError.BadRequest.BAD_REQUEST.getResponseException();
        }

        return true;
    }

    @Transactional
    public String createEmailCheckAuth(User user, String email) {
        EmailCheckAuth emailCheckAuth = new EmailCheckAuth();
        emailCheckAuth.setType(EmailAuthType.RESET_PW_OR_EMAIL);
        emailCheckAuth.setEmail(email);
        emailCheckAuth.setUser(user);

        for(int retry=0;retry<3;retry++) {
            try {
                emailCheckAuth.setToken(
                        UUID.randomUUID().toString().replaceAll("-","")
                                + UUID.randomUUID().toString().replaceAll("-","")
                );
                emailCheckAuthRepository.save(emailCheckAuth);
                if(emailCheckAuth.getSeq() != null) {
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

        if(emailCheckAuth.getSeq() == null) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        boolean isSend = sendButtonedEmail(
                email,
                "비밀번호 찾기 인증메일",
                "<html> <body><h1></h1>"+"<br/>아래 [인증] 버튼을 눌러주세요."+"<form action=\""+serverDomain+ "/email/auth/other-check/" + emailCheckAuth.getToken()+"\"> <input type=\"submit\" value=\"인증\" /> </form>"
                   +"</body></html>"
        );

        if(!isSend) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }

        return emailCheckAuth.getToken();
    }

    @Transactional
    public ResponseEntity<Long> findUsedDt(String token){
        Optional<EmailCheckAuth> emailCheckAuthOptional=emailCheckAuthRepository.findByToken(token);

        if(emailCheckAuthOptional.isEmpty() || emailCheckAuthOptional.get().getUsedDt()==null) {
            throw ResponseError.BadRequest.INVALID_TOKEN.getResponseException();
        }
        Long UserSeq=emailCheckAuthOptional.get().getUser().getSeq();
        return new ResponseEntity<>(UserSeq,HttpStatus.OK);

    }

    @Transactional
    public EmailAuthResult authOtherCheckEmail(String token) {
        Optional<EmailCheckAuth> emailCheckAuthOptional = emailCheckAuthRepository.findByToken(token);

        if(emailCheckAuthOptional.isEmpty()) {
            return EmailAuthResult.INVALID;
        }

        EmailCheckAuth emailCheckAuth = emailCheckAuthOptional.get();

        if(emailCheckAuth.getType() != EmailAuthType.RESET_PW_OR_EMAIL) {
            return EmailAuthResult.INVALID;
        }

        if(emailCheckAuth.getUsedDt() != null) {
            return EmailAuthResult.ALREADY_SUCC;
        }

        Duration duration = Duration.between(emailCheckAuth.getCreatedAt(), LocalDateTime.now());
        boolean isExpired = duration.getSeconds() > emailCheckAuth.getType().getExpiredSeconds();

        if( isExpired ) {
            return EmailAuthResult.INVALID;
        }

        emailCheckAuth.setUsedDt(LocalDateTime.now());
        emailCheckAuthRepository.save(emailCheckAuth);

        return EmailAuthResult.SUCC;
    }
}
