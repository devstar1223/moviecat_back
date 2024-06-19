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
    private long menuId; // 메뉴 id C
    private String vdoNm; // 영화명 C
    private String opngDay; // 개봉일 C
    private int scr; // 평점 C U
    private String vdoEvl; // 영화평 C U
    private String mbrId; // 등록자 id C U D
    private String mbrNm; // 등록자 실명 C U D
}
