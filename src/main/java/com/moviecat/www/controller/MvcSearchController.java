package com.moviecat.www.controller;

import com.moviecat.www.dto.MvcScrBbsDto;
import com.moviecat.www.dto.MvcSearchDto;
import com.moviecat.www.service.MvcSearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MvcSearchController {

    private final MvcSearchService mvcSearchService;

//    @GetMapping("/searchTtl")
//    @Operation(summary = "제목 검색", description = "게시판 번호별 결과 출력. 0번 들어올경우 통합 검색")
//    public ResponseEntity<String> searchTtl(@RequestBody MvcSearchDto mvcSearchDto) {
//        try {
//            String jsonSearchResult = mvcSearchService.searchTtl(mvcSearchDto.getMenuId(), mvcSearchDto.getSrchWord(), mvcSearchDto.getPage(), mvcSearchDto.getLimit());
//            return new ResponseEntity<>(jsonSearchResult, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @GetMapping("/searchTtlCn")
//    @Operation(summary = "제목+내용 검색", description = "게시판 번호별 결과 출력. 0번 들어올경우 통합 검색")
//    public ResponseEntity<String> searchTtlCn(@RequestBody MvcSearchDto mvcSearchDto) {
//        try {
//            String jsonSearchResult = mvcSearchService.searchTtlCn(mvcSearchDto.getMenuId(), mvcSearchDto.getSrchWord(), mvcSearchDto.getPage(), mvcSearchDto.getLimit());
//            return new ResponseEntity<>(jsonSearchResult, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @GetMapping("/searchWriter")
//    @Operation(summary = "작성자 검색", description = "게시판 번호별 결과 출력. 0번 들어올경우 통합 검색")
//    public ResponseEntity<String> searchWriter(@RequestBody MvcSearchDto mvcSearchDto) {
//        try {
//            String jsonSearchResult = mvcSearchService.searchWriter(mvcSearchDto.getMenuId(), mvcSearchDto.getSrchWord(), mvcSearchDto.getPage(), mvcSearchDto.getLimit());
//            return new ResponseEntity<>(jsonSearchResult, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/search")
    @Operation(summary = "게시판 검색", description = "게시판 내에서 검색, 제목1, 제목+내용2, 작성자3")
    public ResponseEntity<String> search(@RequestBody MvcSearchDto mvcSearchDto) {
        try {
            String jsonSearchResult = mvcSearchService.search(mvcSearchDto.getMenuId(), mvcSearchDto.getDiv(), mvcSearchDto.getSrchWord(), mvcSearchDto.getPage(), mvcSearchDto.getLimit());
            return new ResponseEntity<>(jsonSearchResult, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/searchScr")
    @Operation(summary = "영화 평점 검색", description = "영화 이름(또는 영문 이름)으로만 검색합니다.")
    public ResponseEntity<String> searchScr(@RequestBody MvcSearchDto mvcSearchDto) {
        try {
            String jsonSearchResult = mvcSearchService.searchScr(mvcSearchDto.getSrchWord(),mvcSearchDto.getPage(), mvcSearchDto.getLimit());
            return new ResponseEntity<>(jsonSearchResult, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/searchTotal")
    @Operation(summary = "전체 검색", description = "전체 검색, 제목1, 제목+내용2, 작성자3")
    public ResponseEntity<String> searchTotal(@RequestBody MvcSearchDto mvcSearchDto) {
        try {
            String jsonSearchResult = mvcSearchService.searchTotal(mvcSearchDto.getDiv(), mvcSearchDto.getSrchWord(),mvcSearchDto.getPage(), mvcSearchDto.getLimit());
            return new ResponseEntity<>(jsonSearchResult, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
