package com.mozi.moziserver.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.httpException.ResponseError;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class S3ImageService {

    @Autowired
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 파일 업로드 + return URL
    //@Transactional
    public String fileUpload(InputStream inputStream, long contentLength, String contentType, String folderName, String fileName) {
        String key = folderName + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType(contentType);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, metadata);
        amazonS3Client.putObject(putObjectRequest);

        return amazonS3Client.getUrl(bucketName, key).toString();
    }

    public String uploadFile(MultipartFile image, String folderName) {
        final Tika tika = new Tika();
        String mimeTypeString = null;
        try {
            // get extension from file content signiture by tika
            mimeTypeString = tika.detect(image.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }

        // validate image extension
        if (Constant.IMAGE_MIME_TYPE_LIST.stream().noneMatch(mimeTypeString::startsWith)) {
            throw ResponseError.BadRequest.INVALID_IMAGE.getResponseException(
                    "file type must in (" + String.join(",", Constant.IMAGE_MIME_TYPE_LIST) + ")"
            );
        }

        // get file extension ex) png jpeg
        final String extension = mimeTypeString.substring(mimeTypeString.lastIndexOf('/') + 1);

        final String fileName = System.currentTimeMillis()
                + "_" + UUID.randomUUID().toString().replaceAll("-", "")
                + "." + extension;

        String imgUrl = null;
        try {
            imgUrl = fileUpload(image.getInputStream(), image.getSize(), image.getContentType(), folderName, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }

        return imgUrl;
    }

    // 파일명 수정
//    private String fileRename(String originalFileName, String folderName, Long seq) {
//
//        String fileExt = FilenameUtils.getExtension(originalFileName);
//        String fileName = FilenameUtils.getBaseName(originalFileName);
//        Long num = seq + 1;
//
//        return folderName + "/" + fileName + num + '.' + fileExt;
//    }
}
