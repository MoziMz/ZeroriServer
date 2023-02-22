package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.*;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PostboxMessageAdminRepositoryImpl extends QuerydslRepositorySupport implements PostboxMessageAdminRepositorySupport {
    private final QPostboxMessageAdmin qPostboxMessageAdmin = QPostboxMessageAdmin.postboxMessageAdmin;
    private final QUser qUser = QUser.user;
    private final UserRepository userRepository;

    public PostboxMessageAdminRepositoryImpl(UserRepository userRepository) {
        super(PostboxMessageAdmin.class);
        this.userRepository = userRepository;
    }

    @Override
    public List<PostboxMessageAdmin> findAllByUser(
            User user,
            Integer pageSize,
            Long prevLastPostSeq
    ) {
        final Predicate[] predicates = new Predicate[]{
                predicateOptional(qPostboxMessageAdmin.seq::lt, prevLastPostSeq),
        };

        List<PostboxMessageAdmin> postboxMessageAdminList = from(qPostboxMessageAdmin)
                .innerJoin(qPostboxMessageAdmin.user, qUser).fetchJoin()
                .where(predicates)
                .where(qPostboxMessageAdmin.user.eq(user))
                .fetch();

        return postboxMessageAdminList;
    }
    // -------------------- -------------------- below admin methods -------------------- -------------------- //
    @Override
    public List<PostboxMessageAdmin> findAllByKeyword(
            String keyword,
            Long numberOfKeyword,
            Integer pageNumber,
            Integer pageSize
    ){
        return from(qPostboxMessageAdmin)
                .innerJoin(qPostboxMessageAdmin.user,qUser).fetchJoin()
                .where(StringUtils.hasLength(keyword) ?
                        numberOfKeyword != null ?
                                qPostboxMessageAdmin.content.like('%' + keyword + '%').or(qUser.seq.eq(numberOfKeyword)) :
                                qPostboxMessageAdmin.content.like('%' + keyword + '%') :
                        null)
                .offset(pageNumber * pageSize)
                .limit(pageSize)
                .fetch();
    }


    private <T> Predicate predicateOptional(final Function<T, Predicate> whereFunc, final T value) {
        return value != null ? whereFunc.apply(value) : null;
    }
}

