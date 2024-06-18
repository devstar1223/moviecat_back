package com.moviecat.www.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moviecat.www.dto.MvcRcmdtnInfoDto;
import com.moviecat.www.service.MvcRcmdtnInfoService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "추천 기능", description ="추천, 한번 더누르면 추천 취소")
    public ResponseEntity<String> recommend(@ModelAttribute MvcRcmdtnInfoDto mvcRcmdtnInfoDto) throws JsonProcessingException {
        String jsonRcmd = mvcRcmdtnInfoService.recommend(mvcRcmdtnInfoDto);
        try {
            return new ResponseEntity<>(jsonRcmd, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
