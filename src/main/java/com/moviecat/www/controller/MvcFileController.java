package com.moviecat.www.controller;

import com.moviecat.www.service.MvcFileDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MvcFileController {

    private final MvcFileDownloadService mvcFileDownloadService;

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileUrl, @RequestParam String fileName) {
        Resource resource = mvcFileDownloadService.loadFileAsResource(fileUrl);

        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .body(resource);
    }
}
