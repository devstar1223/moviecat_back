package com.moviecat.www.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class MvcProfileImageService {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public MvcProfileImageService(AmazonS3 amazonS3, @Value("${spring.cloud.aws.s3.bucket}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null));
//                .withCannedAcl(CannedAccessControlList.PublicRead)); // ACL PulbicRead로 설정
        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}
