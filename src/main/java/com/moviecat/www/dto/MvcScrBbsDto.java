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
    private long menuId; // 메뉴 id C
    private String vdoNm; // 영화명 C
    private Timestamp opngDay; // 개봉일 C
    private int scr; // 평점 C
    private String vdoEvl; // 영화평 C
    private String mbrId; // 등록자 id C
    private String mbrNm; // 등록자 실명 C
}
