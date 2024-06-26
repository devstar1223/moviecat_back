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
