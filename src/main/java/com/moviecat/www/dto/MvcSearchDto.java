package com.moviecat.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MvcSearchDto {
    private Long menuId; // 게시판 구분(게시판 번호, 전체 검색은 0)
    private int srchCrtr; // 검색 조건 구분 (제목 0, 제목+내용 1, 작성자 2)
    private String srchWord; // 검색어
}
