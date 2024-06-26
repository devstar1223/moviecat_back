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
    public ResponseEntity<String> scrBbsWrite (@RequestBody MvcScrBbsDto mvcScrBbsDto){
        try {
            mvcScrBbsService.scrBbsWrite(mvcScrBbsDto); //.
            return new ResponseEntity<>("작성 성공", HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO. 삭제 필요
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

    //TODO.삭제 필요
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

    @PostMapping("/scrBbsDelete")
    @Operation(summary = "평점 삭제", description = "평점 삭제 api")
    public ResponseEntity<String> scrBbsDelete(@RequestBody MvcScrBbsDto mvcScrBbsDto) {
        mvcScrBbsService.scrBbsDelete(mvcScrBbsDto);
        try{
            return new ResponseEntity<>("평점 삭제 성공", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/scrboard/{menuId}")
    @Operation(summary = "평점 목록 조회", description ="평점 목록 조회")
    public ResponseEntity<String> scrList(@PathVariable("menuId") long menuId
            , @RequestParam(value = "page", defaultValue = "1") int page
            , @RequestParam(value = "mbrId") String mbrId
            , @RequestParam(value = "limit") int limit) {

        try {
            String jsonScrList = mvcScrBbsService.scrList(menuId, mbrId, page, limit);
            return new ResponseEntity<>(jsonScrList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
