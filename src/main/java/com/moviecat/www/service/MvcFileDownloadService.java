package com.moviecat.www.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MvcFileDownloadService {
    private final AmazonS3 amazonS3;

    public Resource loadFileAsResource(String fileUrl) {
        String bucketName = "mvc0605bucket";
        String key = fileUrl.replace("https://mvc0605bucket.s3.ap-northeast-2.amazonaws.com/", "");

        S3Object s3Object = amazonS3.getObject(bucketName, key);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        return new InputStreamResource(inputStream);
    }
}
