package com.moviecat.www.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moviecat.www.dto.MvcAtchFileDto;
import com.moviecat.www.dto.MvcBbsDto;
import com.moviecat.www.service.MvcAtchFileService;
import com.moviecat.www.service.MvcBbsService;
import com.moviecat.www.service.MvcFileUploadService;
import com.moviecat.www.util.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MvcBbsController {
    private final MvcBbsService mvcBbsService;
    private final MvcAtchFileService mvcAtchFileService;

    @PostMapping("/bbsWritePost")
    @Operation(summary = "글 작성", description = "글 작성 api")
    public ResponseEntity<String> bbsWritePost(@RequestPart MvcBbsDto mvcBbsDto, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        MvcAtchFileDto mvcAtchFileDto = null;
        if (files != null && !files.isEmpty()) {
            for (int i = 1; i < files.size() + 1; i++) {
                MultipartFile file = files.get(i - 1);
                mvcAtchFileDto = mvcAtchFileService.writeSetDto(file, mvcBbsDto, i);  // dto를 서비스에서 설정
                mvcAtchFileService.uploadAtchFile(mvcAtchFileDto); // 받은 dto 기반으로 파일 업로드
            }
        }
        if (mvcAtchFileDto != null) {
            mvcBbsDto.setAtchFileId(mvcAtchFileDto.getAtchFileId());
        }
        mvcBbsService.bbsWritePost(mvcBbsDto);
        return new ResponseEntity<>("글 작성 성공", HttpStatus.OK);
    }

    @PatchMapping("/bbsEditPost")
    @Operation(summary = "글 수정", description = "글 수정 api")
    public ResponseEntity<String> bbsEditPost(@RequestPart MvcBbsDto mvcBbsDto, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        if (files != null && !files.isEmpty()) {
            for (int i = 1; i < files.size() + 1; i++) {
                MultipartFile file = files.get(i - 1);
                MvcAtchFileDto mvcAtchFileDto = mvcAtchFileService.editSetDto(file, mvcBbsDto, mvcBbsDto.getAtchFileId());  // dto를 서비스에서 설정
                mvcBbsDto.setAtchFileId(mvcAtchFileDto.getAtchFileId()); // 파일이 있으므로, 파일id가 없었다면 넣어줘야함(반복 되도, 여기 넣어줘야 새로 번호 부여 X)
                mvcAtchFileService.uploadAtchFile(mvcAtchFileDto); // 받은 dto 기반으로 파일 업로드
            }
        }
        mvcBbsService.bbsEditPost(mvcBbsDto);
        return new ResponseEntity<>("글 수정 성공", HttpStatus.OK);
    }

    @DeleteMapping("/bbsDeletePost")
    @Operation(summary = "글 삭제", description = "글 삭제 api, 삭제 유무와 수정 정보만 바꿉니다.")
    public ResponseEntity<String> bbsDeletePost(@RequestBody MvcBbsDto mvcBbsDto) {
        mvcBbsService.bbsDeletePost(mvcBbsDto);
        return new ResponseEntity<>("글 삭제 성공", HttpStatus.OK);
    }

//    @GetMapping("/{boarId}/{pstId}")
//    @Operation(summary = "글 읽기", description = "게시판 번호와 글 번호를 받아 글내용을 반환합니다.")
//    public ResponseEntity<String> bbsReadPost(@PathVariable("pstId") Long pstId) {
//        try {
//            String jsonPost = mvcBbsService.bbsReadPost(pstId);
//            return new ResponseEntity<>(jsonPost, HttpStatus.OK);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("Error processing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/{boardId}")
    @Operation(summary = "게시판 글 목록", description = "/1 처럼 게시판 번호로 요청하면 글 목록 json으로 줍니다. Jackson ObjectMapper 라이브러리 사용했고 공부 필요.")
    public ResponseEntity<String> showBoard(@PathVariable("boardId") Long boardId) {
        try {
            String jsonPostList = mvcBbsService.bbsReadBoard(boardId);
            return new ResponseEntity<>(jsonPostList, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error processing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
