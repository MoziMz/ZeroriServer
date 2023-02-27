package com.mozi.moziserver.adminService;

import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.httpException.ResponseError;
import com.mozi.moziserver.model.entity.DetailIsland;
import com.mozi.moziserver.model.entity.Island;
import com.mozi.moziserver.repository.DetailIslandRepository;
import com.mozi.moziserver.repository.IslandRepository;
import com.mozi.moziserver.repository.UserIslandRepository;
import com.mozi.moziserver.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminIslandService {

    private final S3ImageService s3ImageService;

    private final IslandRepository islandRepository;
    private final DetailIslandRepository detailIslandRepository;
    private final UserIslandRepository userIslandRepository;

    private final PlatformTransactionManager transactionManager;

    // -------------------- -------------------- island -------------------- -------------------- //
    public Island getIsland(Long seq) {

        Island island = islandRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.ISLAND_NOT_EXISTS::getResponseException);

        return island;
    }

    public List<Island> getIslandList() {

        return islandRepository.findAll();
    }

    public void createIsland(
            String name,
            String description,
            Integer openRequiredPoint
    ) {

        final Island island = Island.builder()
                .name(name)
                .description(description)
                .openRequiredPoint(openRequiredPoint)
                .build();

        islandRepository.save(island);
    }

    public void updateIsland(
            Long seq,
            String name,
            String description,
            Integer openRequiredPoint
    ) {

        final Island island = getIsland(seq);

        if (name != null && name.length() != 0) {
            island.setName(name);
        }

        if (description != null && description.length() != 0) {
            island.setDescription(description);
        }

        if (openRequiredPoint != null && openRequiredPoint != 0) {
            island.setOpenRequiredPoint(openRequiredPoint);
        }

        try {
            islandRepository.save(island);
        } catch (Exception e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException();
        }
    }

    public void deleteIsland(Long seq) {

        Island island = getIsland(seq);

        checkLastIsland(island);

        if (!detailIslandRepository.findAllByIsland(island).isEmpty()) {
            throw ResponseError.BadRequest.INVALID_SEQ.getResponseException("detail island remain. Please delete the detail island first.");
        }

        islandRepository.delete(island);
    }

    // -------------------- -------------------- detailIsland -------------------- -------------------- //
    public DetailIsland getDetailIsland(Long seq) {

        DetailIsland detailIsland = detailIslandRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.NOT_EXISTS::getResponseException);

        return detailIsland;
    }

    public List<DetailIsland> getDetailIslandListByIsland(Island island) {

        return detailIslandRepository.findAllByIsland(island);
    }

    public void createDetailIsland(
            Long islandSeq,
            List<MultipartFile> islandImgUrlList,
            List<MultipartFile> islandThumbnailImgUrlList
    ) {
        Island island = getIsland(islandSeq);

        List<DetailIsland> detailIslandList = new ArrayList<>();

        for (Integer animalTurn = 0; animalTurn < Constant.islandMaxLevel; animalTurn++) {

            for (Integer itemTurn = 1; itemTurn <= Constant.lastTurnOfAnimalItem; itemTurn++) {

                String imgUrl = islandUploadFile(islandImgUrlList.get(animalTurn), animalTurn, itemTurn);
                String thumbnailImgUrl = islandUploadFile(islandThumbnailImgUrlList.get(animalTurn), animalTurn, itemTurn);

                if (animalTurn == 0) {
                    itemTurn = 0;
                }

                final DetailIsland detailIsland = DetailIsland.builder()
                        .animalTurn(animalTurn)
                        .itemTurn(itemTurn)
                        .imgUrl(imgUrl)
                        .thumbnailImgUrl(thumbnailImgUrl)
                        .island(island)
                        .build();

                detailIslandList.add(detailIsland);

                if (animalTurn == 0) {
                    break;
                }
            }
        }

        detailIslandRepository.saveAll(detailIslandList);
    }

    public void updateImgOfDetailIsland(Long seq, MultipartFile img, MultipartFile islandThumbnailImg) {

        DetailIsland detailIsland = detailIslandRepository.findById(seq)
                .orElseThrow(ResponseError.NotFound.NOT_EXISTS::getResponseException);

        String imgUrl = null;
        if (img != null) {
            try {
                imgUrl = islandUploadFile(img, detailIsland.getAnimalTurn(), detailIsland.getItemTurn());
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            detailIsland.setImgUrl(imgUrl);
        }

        String islandThumbnailImgUrl = null;
        if (islandThumbnailImg != null) {
            try {
                islandThumbnailImgUrl = islandUploadFile(islandThumbnailImg, detailIsland.getAnimalTurn(), detailIsland.getItemTurn());
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
            detailIsland.setThumbnailImgUrl(islandThumbnailImgUrl);
        }

        detailIslandRepository.save(detailIsland);
    }

    public void deleteDetailIsland(Long seq) {

        DetailIsland detailIsland = getDetailIsland(seq);

        checkLastIsland(detailIsland.getIsland());

        if (userIslandRepository.existsByDetailIsland(detailIsland)) {
            throw ResponseError.BadRequest.INVALID_SEQ.getResponseException();
        }

        detailIslandRepository.delete(detailIsland);
    }

    public void checkLastIsland(Island island) {

        if (island.getSeq() == Constant.lastIslandSeq) {
            throw ResponseError.BadRequest.INVALID_SEQ.getResponseException("Creation/Deletion is not possible because the island seq is 1.");
        }
    }

    public String islandUploadFile(MultipartFile multipartFile, Integer animalTurn, Integer itemTurn) {
        try {
            return s3ImageService.uploadFile(multipartFile, "DetailIsland_animal" + animalTurn + "_item" + itemTurn);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private void withTransaction(Runnable runnable) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();

        TransactionStatus status = transactionManager.getTransaction(definition);
        try {
            runnable.run();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
        }
    }
}
