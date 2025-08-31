package com.mozi.moziserver.service;

import com.mozi.moziserver.httpException.ResponseError;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageStorageService {
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM");
    private final Tika tika = new Tika();
    @Value("${storage.base-dir}")
    private String baseDirStr;
    @Value("${storage.directories.image}")
    private String imageDirStr;
    @Value("${server.assets-domain}")
    private String assetsDomain;

    public String writeImageFile(final MultipartFile multipartFile, final String subDir) {
        final String extension;
        try {
            extension = getImageExtension(multipartFile.getBytes());
        } catch (IOException e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("multipartFile.getBytes() IOException" + e.getMessage());
        }

        final String relativePath = buildRelativePath(subDir, extension);

        final Path fullPath = Paths.get(baseDirStr, imageDirStr, relativePath);
        if (!Files.exists(fullPath.getParent())) {
            try {
                Files.createDirectories(fullPath.getParent());
            } catch (UnsupportedOperationException e) {
                throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("Files.createDirectories UnsupportedOperationException " + e.getMessage());
            } catch (FileAlreadyExistsException ignored) {
            } catch (IOException e) {
                throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("fail to upload file ( Files.createDirectories IOException " + e.getMessage() + ")");
            }
        } else if (Files.exists(fullPath)) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("fail to upload file");
        }

        try {
            Files.copy(multipartFile.getInputStream(), fullPath);
        } catch (FileAlreadyExistsException e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("fail to upload file ( Files.copy FileAlreadyExistsException " + e.getMessage() + ")");
        } catch (DirectoryNotEmptyException e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("fail to upload file ( Files.copy DirectoryNotEmptyException " + e.getMessage() + ")");
        } catch (UnsupportedOperationException e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("Files.copy UnsupportedOperationException " + e.getMessage());
        } catch (IOException e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("fail to upload file ( Files.copy IOException " + e.getMessage() + ")");
        }

        return assetsDomain + imageDirStr + relativePath;
    }

    public void deleteImageFile(final String filePathStr) {
        final Path path = Paths.get(baseDirStr, imageDirStr, filePathStr);
        if (!Files.exists(path)) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("not exists file");
        }
        try {
            Files.delete(path);
        } catch (NoSuchFileException e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("fail to delete file ( NoSuchFileException " + e.getMessage() + ")");
        } catch (DirectoryNotEmptyException e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("fail to delete file ( DirectoryNotEmptyException " + e.getMessage() + ")");
        } catch (IOException e) {
            throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("fail to delete file ( IOException " + e.getMessage() + ")");
        }
    }

    private String getImageExtension(final byte[] fileBytes) {
        final String mimeType = tika.detect(fileBytes);
        switch (mimeType) {
            case MimeTypeUtils.IMAGE_GIF_VALUE:
                return ".gif";
            case MimeTypeUtils.IMAGE_JPEG_VALUE:
                return ".jpg";
            case MimeTypeUtils.IMAGE_PNG_VALUE:
                return ".png";
            default:
                break;
        }
        throw ResponseError.InternalServerError.UNEXPECTED_ERROR.getResponseException("getImageExtension unexpected mimeType = " + mimeType);
    }

    private String buildRelativePath(final String subDir, final String extension) {
        String yearMonth = LocalDate.now().format(YEAR_MONTH_FORMATTER);
        String fileName = generateUniqueFileName();
        return "/"+String.join("/", subDir, yearMonth, fileName + extension);
    }

    private String generateUniqueFileName() {
        return Instant.now().getEpochSecond() + "_" + UUID.randomUUID().toString().replace("-", "");
    }
}
