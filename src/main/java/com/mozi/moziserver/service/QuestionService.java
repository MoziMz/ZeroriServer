package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Question;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserAuth;
import com.mozi.moziserver.model.req.ReqQuestionCreate;
import com.mozi.moziserver.repository.QuestionRepository;
import com.mozi.moziserver.repository.UserAuthRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    private final S3ImageService s3ImageService;

    @Transactional
    public void createQuestion(Long userSeq, ReqQuestionCreate reqQuestionCreate){

        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        Long seq = questionRepository.findSeq();

        String imgUrl = ""; // TODO s3ImageService.fileUpload(reqQuestionCreate.getImgUrl(), "question", seq);


        Question question=Question.builder()
                .user(user)
                .email(reqQuestionCreate.getEmail())
                .category(reqQuestionCreate.getQuestionCategory())
                .title(reqQuestionCreate.getTitle())
                .content(reqQuestionCreate.getContent())
                .imgUrl(imgUrl)
                .build();

        try {
            questionRepository.save(question);
        } catch (Exception e) {
            throw ResponseError.BadRequest.ALREADY_CREATED.getResponseException(); // for duplicate exception
        }

    }

}