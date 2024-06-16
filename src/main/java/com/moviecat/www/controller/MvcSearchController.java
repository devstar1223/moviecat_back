package com.moviecat.www.controller;

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

    @GetMapping("/searchAll") // 우선 모든 데이터 반환하는 api 부터 만들고.
    @Operation(summary = "검색(임시)", description = "게시판 구분(게시판 번호, 전체 검색은 0), 검색 조건 구분 (제목 0, 제목+내용 1, 작성자 2)")
    public ResponseEntity<String> searchAll(@RequestBody MvcSearchDto mvcSearchDto){
        String jsonSearchResult = mvcSearchService.searchAll(mvcSearchDto.getMenuId(),mvcSearchDto.getSrchCrtr(),mvcSearchDto.getSrchWord());
        if (jsonSearchResult == null) {
            return new ResponseEntity<>("검색결과가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(jsonSearchResult, HttpStatus.OK);
    }
}