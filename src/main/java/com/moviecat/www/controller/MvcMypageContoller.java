package com.moviecat.www.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moviecat.www.dto.MvcMbrInfoDto;
import com.moviecat.www.dto.MvcRcmdtnInfoDto;
import com.moviecat.www.service.MvcMyInfoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MvcMypageContoller {
    private final MvcMyInfoService mvcMyInfoService;
    @GetMapping("/myPostRead")
    @Operation(summary = "내 게시글 보기", description ="게시판명, 스포, 제목, 댓글, 추천, 작성일")
    public ResponseEntity<String> myPostRead(@RequestParam String mbrId,@RequestParam(value = "page", defaultValue = "1") int page, int limit){
        try {
            String jsonMyPost = mvcMyInfoService.myPostRead(mbrId,page, limit);
            return new ResponseEntity<>(jsonMyPost, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/myCmntRead")
    @Operation(summary = "내 댓글 보기", description ="게시판명, 댓글, 작성일")
    public ResponseEntity<String> myCmntRead(@RequestParam String mbrId,@RequestParam(value = "page", defaultValue = "1") int page, int limit){
        try {
            String jsonMyCmnt = mvcMyInfoService.myCmntRead(mbrId,page, limit);
            return new ResponseEntity<>(jsonMyCmnt, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/myScrRead")
    @Operation(summary = "내 평점 보기", description ="영화명, 평점, 작성일")
    public ResponseEntity<String> myScrRead(@RequestParam String mbrId,@RequestParam(value = "page", defaultValue = "1") int page, int limit){
        try {
            String jsonMyScr = mvcMyInfoService.myScrRead(mbrId,page,limit);
            return new ResponseEntity<>(jsonMyScr, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
