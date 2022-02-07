package com.mozi.moziserver.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserStickerImg {
   private StickerImg stickerImg;

   private boolean downloaded;
}
