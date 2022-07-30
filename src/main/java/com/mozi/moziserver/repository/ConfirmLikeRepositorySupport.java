package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.ConfirmLike;
import com.mozi.moziserver.model.entity.User;

import java.util.List;

public interface ConfirmLikeRepositorySupport {
    List<ConfirmLike> findAllByUserAndConfirmsIn(User user, List<Confirm> confirmList);
}
