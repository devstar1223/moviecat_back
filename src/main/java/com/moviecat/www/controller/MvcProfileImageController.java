package com.moviecat.www.controller;

import com.moviecat.www.service.MvcProfileImageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MvcProfileImageController {

    private final MvcProfileImageService mvcProfileImageService;
    @Operation(summary = "프로필 사진 업로드", description = "업로드 성공시 s3 링크와 함께 200 OK 반환, 실패시 500 에러 반환")
    @PostMapping("/uploadProfileImage")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String url = mvcProfileImageService.uploadFile(file);
            return new ResponseEntity<>(url, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
