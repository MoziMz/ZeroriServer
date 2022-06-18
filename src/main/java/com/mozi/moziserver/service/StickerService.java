package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.Animal;
import com.mozi.moziserver.model.entity.Sticker;
import com.mozi.moziserver.repository.StickerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StickerService {
    private final StickerRepository stickerRepository;
    private final S3ImageService s3ImageService;
    private Sticker getSticker(Long seq) {
        Sticker sticker = stickerRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.STICKER_NOT_EXISTS::getResponseException);

        return sticker;
    }

    public void createSticker(MultipartFile image) {
        String imgUrl = null;
        try {
            imgUrl = s3ImageService.uploadFile(image, "confirm");
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        Sticker sticker = Sticker.builder()
                .imgUrl(imgUrl)
                .build();

        stickerRepository.save(sticker);

        //TODO 여러번 save가 되어버리면?
    }

    public void updateSticker(Long seq, MultipartFile image) {
        Sticker sticker = getSticker(seq);

        String imgUrl = null;
        try {
            imgUrl = s3ImageService.uploadFile(image, "confirm");
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }

        sticker.setImgUrl(imgUrl);

        stickerRepository.save(sticker);
    }
}
