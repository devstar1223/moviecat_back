package com.moviecat.www.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class MvcScrBbsDto {
    private long scrId; // 평점 id U D
    private long menuId; // 메뉴 id C + 검색시 사용
    private String vdoId; //TODO.영화 id
    private String vdoNm; // 영화명 C + 검색시 사용
    private String vdoEnNm; //TODO.영화영어명
    private String opngDay; //TODO.openYear로 변경 필요(변경 후 얘기해주세요) 개봉일 C
    private int scr; // 평점 C U
    private String vdoEvl; // 영화평 C U
    private String mbrId; // 등록자 id C U D
    private String mbrNm; // 등록자 실명 C U D
}
