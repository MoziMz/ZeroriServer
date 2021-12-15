package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.PostboxMessageAdmin;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.req.ReqBasic;
import com.mozi.moziserver.repository.PostboxMessageAdminRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostboxMessageAdminService {
    private final UserRepository userRepository;
    private final PostboxMessageAdminRepository postboxMessageAdminRepository;

    public List<PostboxMessageAdmin> getPostboxMessageAdminList(Long userSeq, ReqBasic req) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        return postboxMessageAdminRepository.findAllByUser(
                user,
                req.getPageSize(),
                req.getPrevLastPostSeq()
        );
    }
}
