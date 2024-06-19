package com.moviecat.www.controller;

import com.moviecat.www.dto.MvcScrBbsDto;
import com.moviecat.www.service.MvcScrBbsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MvcScrBbsController {

    private final MvcScrBbsService mvcScrBbsService;

    @PostMapping("/scrBbsWrite")
    @Operation(summary = "평점 작성", description ="평점 작성")
    public ResponseEntity<String> scrBbsWrite (@ModelAttribute MvcScrBbsDto mvcScrBbsDto){
        try {
            mvcScrBbsService.scrBbsWrite(mvcScrBbsDto); //.
            return new ResponseEntity<>("작성 성공", HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/scrboard/{menuId}/{scrId}")
    @Operation(summary = "평점 글 조회", description ="평점 글 조회")
    public ResponseEntity<String> scrBbsRead(@PathVariable("scrId") long scrId) {
        try {
            String jsonScr = mvcScrBbsService.scrBbsRead(scrId);
            return new ResponseEntity<>(jsonScr, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/scrBbsEdit")
    @Operation(summary = "평점 수정", description = "평점 수정 api")
    public ResponseEntity<String> scrBbsEdit(@ModelAttribute MvcScrBbsDto mvcScrBbsDto) {
        mvcScrBbsService.scrBbsEdit(mvcScrBbsDto);
        try{
            return new ResponseEntity<>("평점 수정 성공", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/scrBbsDelete")
    @Operation(summary = "평점 삭제", description = "평점 삭제 api")
    public ResponseEntity<String> scrBbsDelete(@ModelAttribute MvcScrBbsDto mvcScrBbsDto) {
        mvcScrBbsService.scrBbsDelete(mvcScrBbsDto);
        try{
            return new ResponseEntity<>("평점 삭제 성공", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}
