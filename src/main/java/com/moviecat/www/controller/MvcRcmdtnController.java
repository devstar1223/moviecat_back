package com.moviecat.www.controller;

import com.moviecat.www.dto.MvcRcmdtnInfoDto;
import com.moviecat.www.service.MvcRcmdtnInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MvcRcmdtnController {
    private final MvcRcmdtnInfoService mvcRcmdtnInfoService;

    @PostMapping("/recommend")
    public ResponseEntity<String> recommend(@RequestBody MvcRcmdtnInfoDto mvcRcmdtnInfoDto){
        mvcRcmdtnInfoService.recommend(mvcRcmdtnInfoDto);
        try {
            return new ResponseEntity<>("추천/추천 취소 성공", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
