package com.moviecat.www.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class MvcFileUploadService {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public MvcFileUploadService(AmazonS3 amazonS3, @Value("${spring.cloud.aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    @Transactional
    public String uploadFile(MultipartFile file/*, String folderName*/) {
        String fileName = /*folderName + "/" + */UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null));
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            // 예외 전파
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }
    }
}
