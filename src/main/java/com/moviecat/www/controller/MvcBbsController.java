package com.moviecat.www.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.dto.MvcAtchFileDto;
import com.moviecat.www.dto.MvcBbsDto;
import com.moviecat.www.service.MvcAtchFileService;
import com.moviecat.www.service.MvcBbsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MvcBbsController {
    private final MvcBbsService mvcBbsService;
    private final MvcAtchFileService mvcAtchFileService;

    @PostMapping("/bbsWritePost")
    @Operation(summary = "글 작성", description = "글 작성 api")
    public ResponseEntity<String> bbsWritePost(@ModelAttribute MvcBbsDto mvcBbsDto, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        MvcAtchFileDto mvcAtchFileDto = null;
        try {
            if (files != null && !files.isEmpty()) {
                for (int i = 1; i < files.size() + 1; i++) {
                    MultipartFile file = files.get(i - 1);
                    mvcAtchFileDto = mvcAtchFileService.writeSetDtoAndUploadFile(file, mvcBbsDto, i);  // dto를 서비스에서 설정하고, dto를 참고하여 파일 업로드
                    mvcAtchFileService.insertAtchFileTable(mvcAtchFileDto); // 받은 dto 기반으로 DB에 저장
                }
            }
            if (mvcAtchFileDto != null) {
                mvcBbsDto.setAtchFileId(mvcAtchFileDto.getAtchFileId());
            }
            mvcBbsService.bbsWritePost(mvcBbsDto);
            return new ResponseEntity<>("글 작성 성공", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/bbsEditPost")
    @Operation(summary = "글 수정", description = "글 수정 api, 파일 id 무조건 들어와야, 새 파일 id 만들어지지 않습니다.")
    public ResponseEntity<String> bbsEditPost(@ModelAttribute MvcBbsDto mvcBbsDto
            , @RequestPart(value = "files", required = false) List<MultipartFile> files
            , @RequestParam Map<String, String> allRequestParams) {

        //TODO. 삭제할 파일 정보들입니다. start
        List<Map<String, Integer>> delFileList = new ArrayList<>();

        // delFileList 파싱
        for (int i = 0; ; i++) {
            String fileIdKey = "delFileList[" + i + "][fileId]";
            String seqKey = "delFileList[" + i + "][seq]";
            if (!allRequestParams.containsKey(fileIdKey) || !allRequestParams.containsKey(seqKey)) {
                break;
            }
            //Map<String, Integer> 형식(fileId, seq)
            Map<String, Integer> fileMap = Map.of(
                    "fileId", Integer.parseInt(allRequestParams.get(fileIdKey)),
                    "seq", Integer.parseInt(allRequestParams.get(seqKey))
            );
            //Map을 list에 담음.
            delFileList.add(fileMap);
        }

        mvcBbsDto.setDelFileList(delFileList);
        //TODO.end

//        if (files != null && !files.isEmpty()) {
//            for (int i = 1; i < files.size() + 1; i++) {
//                MultipartFile file = files.get(i - 1);
//                MvcAtchFileDto mvcAtchFileDto = mvcAtchFileService.editSetDtoAndUploadFile(file, mvcBbsDto, mvcBbsDto.getAtchFileId());  // // dto를 서비스에서 설정하고, dto를 참고하여 파일 업로드
//                mvcBbsDto.setAtchFileId(mvcAtchFileDto.getAtchFileId()); // 파일이 있으므로, 파일id가 없었다면 넣어줘야함(반복 되도, 여기 넣어줘야 새로 번호 부여 X)
//                mvcAtchFileService.insertAtchFileTable(mvcAtchFileDto); // 받은 dto 기반으로 DB에 저장
//            }
//        }
//        mvcBbsService.bbsEditPost(mvcBbsDto);
        return new ResponseEntity<>("글 수정 성공", HttpStatus.OK);
    }

    @PostMapping("/bbsDeletePost")
    @Operation(summary = "글 삭제", description = "글 삭제 api, 삭제 유무와 수정 정보만 바꿉니다.")
    public ResponseEntity<String> bbsDeletePost(@RequestBody MvcBbsDto mvcBbsDto) {
        mvcBbsService.bbsDeletePost(mvcBbsDto);
        return new ResponseEntity<>("글 삭제 성공", HttpStatus.OK);
    }

    @GetMapping("/movieboard/{menuId}")
    @Operation(summary = "게시판 글 목록", description = "/1 처럼 게시판 번호로 요청하면 글 목록 json으로 줍니다. 페이지도 줘야합니다 (기본 1)")
    public ResponseEntity<String> bbsList(@PathVariable("menuId") Long menuId,@RequestParam(value = "page", defaultValue = "1") int page) {
        try {
            String jsonPostList = mvcBbsService.bbsReadBoard(menuId, page);
            return new ResponseEntity<>(jsonPostList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/movieboard/{menuId}/{pstId}")
    @Operation(summary = "글 상세 페이지", description = "게시판 번호와 글 번호를 받아 글내용을 json으로 반환합니다.")
    public ResponseEntity<String> bbsReadPost(@PathVariable("menuId") long menuId,@PathVariable("pstId") long pstId) {
        try {
            String jsonPost = mvcBbsService.bbsReadPost(menuId,pstId);
            return new ResponseEntity<>(jsonPost, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/movieboard/{menuId}/{pstId}/files")
    @Operation(summary = "글 상세 페이지-첨부파일", description = "게시판 번호와 글 번호를 받아 첨부파일을 List로 반환합니다.")
    public ResponseEntity<Object> bbsReadPostFiles(@PathVariable("menuId") long menuId, @PathVariable("pstId") long pstId) {
        try {
            Map<String,Object> atchFileList = mvcBbsService.bbsReadPostFiles(pstId);
            return ResponseEntity.ok().body(atchFileList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("에러");
        }
    }
}
