package com.moviecat.www.controller;

import com.moviecat.www.dto.MvcBbsDto;
import com.moviecat.www.service.MvcBbsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
