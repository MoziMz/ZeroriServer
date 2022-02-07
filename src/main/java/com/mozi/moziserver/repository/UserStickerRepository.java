package com.mozi.moziserver.repository;

import com.mozi.moziserver.model.entity.UserSticker;
import com.mozi.moziserver.model.entity.UserStickerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStickerRepository extends JpaRepository<UserSticker, UserStickerId>, UserStickerRepositorySupport{
}
