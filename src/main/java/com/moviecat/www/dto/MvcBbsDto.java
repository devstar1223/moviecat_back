package com.moviecat.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class MvcBbsDto {
    private long pstId; // 게시글 id
    private long menuId; // 메뉴 id
    private String ttl; // 제목
    private String cn; // 내용
    private Long atchFileUrl; // 첨부파일 id
    private String spoYn; // 스포일러 유무
    private String rgstUserId; // 등록 ID
    private String rgstUserNm; // 등록자
    private Timestamp rgstDay; // 등록일
    private String mdfcnUserId; // 수정 ID
    private String mdfcnUserNm; // 수정자
    private Timestamp mdfcnDay; // 수정일
    private String deltYn; // 삭제유무
}
