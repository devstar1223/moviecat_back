package com.moviecat.www.controller;

import com.moviecat.www.dto.MvcCmntDto;
import com.moviecat.www.entity.MvcBbsCmnt;
import com.moviecat.www.service.MvcBbsCmntService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MvcBbsCmntController {

    private final MvcBbsCmntService mvcBbsCmntService;
    @PostMapping("/bbsWriteCmnt")
    @Operation(summary = "댓글 작성", description ="답글 X")
    public ResponseEntity<String> bbsWriteCmnt(@ModelAttribute MvcCmntDto mvcCmntDto){
        try {
            mvcBbsCmntService.bbsWriteCmnt(mvcCmntDto);
            return new ResponseEntity<>("댓글 작성 성공", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("댓글 작성 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/bbsWriteReply")
    @Operation(summary = "답글 작성", description ="ㅇㅇㅇㅇㅇㅇㅇ")
    public ResponseEntity<String> bbsWriteReply(@ModelAttribute MvcCmntDto mvcCmntDto){
        try {
            mvcBbsCmntService.bbsWriteReply(mvcCmntDto);
            return new ResponseEntity<>("답글 작성 성공", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("답글 작성 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/bbsEditCmnt")
    @Operation(summary = "댓글 수정", description ="ㅇㅇㅇㅇㅇㅇㅇ")
    public ResponseEntity<String> bbsEditCmnt(@ModelAttribute MvcCmntDto mvcCmntDto){
        try {
            mvcBbsCmntService.bbsEditCmnt(mvcCmntDto);
            return new ResponseEntity<>("댓글 수정 성공", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("댓글 수정 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/bbsDeleteCmnt")
    @Operation(summary = "댓글/답글 삭제", description ="댓글/답글 통합 삭제")
    public ResponseEntity<String> bbsDeleteCmnt(@RequestParam long cmntId){
        try {
            mvcBbsCmntService.bbsDeleteCmnt(cmntId);
            return new ResponseEntity<>("댓글 삭제 성공", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("댓글 삭제 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/movieboard/{menuId}/{pstId}/cmnt")
    @Operation(summary = "글 읽기-댓글", description = "댓글의 수와 댓글내용을 json으로 반환합니다.")
    public ResponseEntity<String> bbsReadCmnt(@PathVariable("pstId") long pstId) {
        try {
            String jsonCmnt = mvcBbsCmntService.bbsReadCmnt(pstId);
            return new ResponseEntity<>(jsonCmnt, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("에러", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
