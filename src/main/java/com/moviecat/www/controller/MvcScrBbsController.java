package com.moviecat.www.controller;

import com.moviecat.www.dto.MvcScrBbsDto;
import com.moviecat.www.service.MvcScrBbsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MvcScrBbsController {

    private final MvcScrBbsService mvcScrBbsService;

    @PostMapping("/scrBbsWrite")
    @Operation(summary = "평점 작성", description ="평점 작성")
    public ResponseEntity<String> scrBbsWrite (@ModelAttribute MvcScrBbsDto mvcScrBbsDto){
        try {
            mvcScrBbsService.scrBbsWrite(mvcScrBbsDto);
            return new ResponseEntity<>("작성 성공", HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
