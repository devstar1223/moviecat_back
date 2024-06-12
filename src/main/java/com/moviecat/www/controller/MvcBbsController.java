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
    private final MvcFileUploadService mvcFileUploadService;
    private final MvcAtchFileService mvcAtchFileService;

    @PostMapping("/bbsWritePost")
    @Operation(summary = "글 작성", description = "글 작성 api")
    // TODO. 현재 컨트롤레엇 dto에 전부 설정한 후, db에는 dto의 정보대로 또다시 등록하는 비효율적인 방식 / 수정예정.
    public ResponseEntity<String> bbsWritePost(@RequestPart MvcBbsDto mvcBbsDto, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        mvcBbsService.bbsWritePost(mvcBbsDto);
        if (!files.isEmpty()) {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                MvcAtchFileDto newFileDto = new MvcAtchFileDto();
                newFileDto.setSeq(i + 1);
                newFileDto.setMultipartFile(file);
                newFileDto.setActlFileNm(file.getOriginalFilename());
                newFileDto.setStrgFileNm("임시 이름"); //TODO. S3 저장 파일명 규칙 생성 예정
                newFileDto.setStrgFilePath(mvcFileUploadService.uploadFile(file)); // 파일업로드 서비스에서 등록하고 주소명 반환
                newFileDto.setStrgFileSize((int) file.getSize());
                newFileDto.setStrgFileExtn(FileUtils.getFileExtension(file.getOriginalFilename())); // 파일 확장자 추출은 util에서
                newFileDto.setRgstUserId(mvcBbsDto.getRgstUserId()); // 글 등록자 ID와 같음
                newFileDto.setRgstUserNm(mvcBbsDto.getRgstUserNm()); // 글 등록자 이름과 같음
                newFileDto.setRgstDay(Timestamp.valueOf(LocalDateTime.now())); // 현재시간
                newFileDto.setMdfcnUserId(mvcBbsDto.getRgstUserId()); // 글 등록자 ID와 같음
                newFileDto.setMdfcnUserNm(mvcBbsDto.getRgstUserNm()); // 글 등록자 이름과 같음
                newFileDto.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 현재시간
                newFileDto.setDeltYn("N"); // 등록시엔 삭제유무 기본 N
                System.out.println(newFileDto.toString());
                mvcAtchFileService.uploadAtchFile(newFileDto); // 하나씩 DB에 저장
            }
        }
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
