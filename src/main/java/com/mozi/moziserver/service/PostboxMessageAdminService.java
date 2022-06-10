package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.PostboxMessageAdmin;
import com.mozi.moziserver.model.entity.User;
import com.mozi.moziserver.model.entity.UserChallenge;
import com.mozi.moziserver.model.req.ReqList;
import com.mozi.moziserver.repository.PostboxMessageAdminRepository;
import com.mozi.moziserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostboxMessageAdminService {
    private final UserRepository userRepository;
    private final PostboxMessageAdminRepository postboxMessageAdminRepository;

    private PostboxMessageAdmin getPostboxMessageAdmin(Long userSeq, Long seq) {
        PostboxMessageAdmin postboxMessageAdmin = postboxMessageAdminRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.POSTBOX_MESSAGE_ADMIN_NOT_EXISTS::getResponseException);

        if (!postboxMessageAdmin.getUser().getSeq().equals(userSeq)) {
            throw ResponseError.Forbidden.NO_AUTHORITY.getResponseException();
        }

        return postboxMessageAdmin;
    }

    public List<PostboxMessageAdmin> getPostboxMessageAdminList(Long userSeq, ReqList req) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(ResponseError.NotFound.USER_NOT_EXISTS::getResponseException);

        return postboxMessageAdminRepository.findAllByUser(
                user,
                req.getPageSize(),
                req.getPrevLastSeq()
        );
    }

    @Transactional
    public void checkMessage(Long userSeq, Long seq) {
        final PostboxMessageAdmin postboxMessageAdmin = getPostboxMessageAdmin(userSeq, seq);
        postboxMessageAdmin.setCheckedState(true);

        postboxMessageAdminRepository.save(postboxMessageAdmin);
    }
}
