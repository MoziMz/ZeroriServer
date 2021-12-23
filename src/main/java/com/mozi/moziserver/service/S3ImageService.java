package com.mozi.moziserver.service;

import com.amazonaws.services.s3.model.*;
import com.mozi.moziserver.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File;


@Service
@RequiredArgsConstructor
public class S3ImageService {

    @Autowired
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 파일 업로드 + return URL
    public String fileUpload(String filePath, String folderName, Long seq) {

        File file = new File(filePath);
        String key = fileRename(file.getName(), folderName, seq);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        amazonS3Client.putObject(putObjectRequest);

        return amazonS3Client.getUrl(bucketName, key).toString();
    }

    // 파일명 수정
    private String fileRename(String originalFileName, String folderName, Long seq) {

        String fileExt = FilenameUtils.getExtension(originalFileName);
        String fileName = FilenameUtils.getBaseName(originalFileName);
        Long num = seq + 1;

        return folderName + "/" + fileName + num + '.' + fileExt;
    }
}
