package com.mozi.moziserver.controller;

import com.mozi.moziserver.model.entity.Question;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.req.ReqQuestionCreate;
import com.mozi.moziserver.security.SessionUser;
import com.mozi.moziserver.service.QuestionService;
import com.mozi.moziserver.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConfigurationController {

    private final QuestionService questionService;

    @ApiOperation("문의 등록")
    @PostMapping("/v1/users/v2/question")
    public ResponseEntity<Void> createQuestion(
            @ApiParam(hidden = true) @SessionUser Long userSeq,
            @RequestBody @Valid ReqQuestionCreate reqQuestionCreate
    ) {
        questionService.createQuestion(userSeq, reqQuestionCreate);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @ApiOperation("프로필 편집")
//    @PostMapping(value = "/v1/users/configuration/profile")
//    public ResponseEntity<Void> updateProductQuestionAnswer(
//            @ApiIgnore @SessionUser User user,
//            @RequestBody @Valid ReqQuestionCreate reqQuestionCreate
//    ) {
//        productQuestionService.updateProductQuestion(user, seq, productQuestionSeq,reqProductQuestionCreate);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
