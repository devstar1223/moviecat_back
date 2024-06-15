package com.moviecat.www.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.moviecat.www.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MvcFileUploadService {

    private final AmazonS3 amazonS3;
    private final String bucketName;
    private final FileUtils fileUtils;

    @Autowired
    public MvcFileUploadService(AmazonS3 amazonS3, @Value("${spring.cloud.aws.s3.bucket}") String bucketName, FileUtils fileUtils) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
        this.fileUtils = fileUtils;
    }

    @Transactional
    public String[] uploadFile(MultipartFile file, String folderName) { // 파일과 저장 폴더 받아옴

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 현재 시간 숫자로만 포맷
        String nowTime = sdf.format(Timestamp.valueOf(LocalDateTime.now())); // 현재 시간 위 포맷 으로 바꿔서 String형으로

        String shortUuid = UUID.randomUUID().toString().substring(0, 5); // uuid 앞 5글자만 사용
        String fileExtension = fileUtils.getFileExtension(file.getOriginalFilename()); // 확장자 추출 util
        String strgFileName = shortUuid + "_" + nowTime; // 경로와 확장자 없는, s3에 저장되는 파일 이름
        String uploadFileName = folderName + "/" + strgFileName + "." + fileExtension; // 경로와 확장자를 포함한 s3 저장 이름
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, uploadFileName, file.getInputStream(), null));
            return new String[] {strgFileName,fileExtension,amazonS3.getUrl(bucketName, uploadFileName).toString()}; //{파일 이름, 파일 확장자, 파일 주소} 반환
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }
    }
}
