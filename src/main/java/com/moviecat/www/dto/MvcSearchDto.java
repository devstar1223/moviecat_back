package com.moviecat.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MvcSearchDto {
    private Long menuId; // 게시판 구분(게시판 번호, 전체 검색은 0)
    private int div; // 제목(1), 제목+내용(2), 작성자 검색 구분(3)
    private String srchWord; // 검색어
    private int page; // 페이지
    private int limit; // 한번에 보여줄 양
}
