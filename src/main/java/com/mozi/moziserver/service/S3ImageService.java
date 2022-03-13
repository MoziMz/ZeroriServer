package com.mozi.moziserver.service;

import com.amazonaws.services.s3.model.*;
import com.mozi.moziserver.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;


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
