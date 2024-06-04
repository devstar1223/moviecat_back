package com.moviecat.www.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.dto.MvcBbsDto;
import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.service.MvcBbsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MvcBbsController {
    private final MvcBbsService mvcBbsService;

    @PostMapping("/bbsWritePost")
    @Operation(summary = "글 작성", description = "글 작성 api")
    public ResponseEntity<String> bbsWritePost(@RequestBody MvcBbsDto mvcBbsDto) {
        mvcBbsService.bbsWritePost(mvcBbsDto);
        return new ResponseEntity<>("글 작성 성공", HttpStatus.OK);
    }

    @PatchMapping("/bbsEditPost")
    @Operation(summary = "글 수정", description = "글 수정 api")
    public ResponseEntity<String> bbsEditPost(@RequestBody MvcBbsDto mvcBbsDto) {
        mvcBbsService.bbsEditPost(mvcBbsDto);
        return new ResponseEntity<>("글 수정 성공", HttpStatus.OK);
    }

    @DeleteMapping("/bbsDeletePost")
    @Operation(summary = "글 삭제", description = "글 삭제 api, 삭제 유무와 수정 정보만 바꿉니다.")
    public ResponseEntity<String> bbsDeletePost(@RequestBody MvcBbsDto mvcBbsDto) {
        mvcBbsService.bbsDeletePost(mvcBbsDto);
        return new ResponseEntity<>("글 삭제 성공", HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "게시판 글 목록", description = "/1 처럼 게시판 번호로 요청하면 글 목록 json으로 줍니다. Jackson ObjectMapper 라이브러리 사용했고 공부 필요.")
    public ResponseEntity<String> showBoard(@PathVariable("boardId") Long boardId) {
        try {
            String jsonPostList = mvcBbsService.bbsReadBoard(boardId);
            return ResponseEntity.ok(jsonPostList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON");
        }
    }
}
