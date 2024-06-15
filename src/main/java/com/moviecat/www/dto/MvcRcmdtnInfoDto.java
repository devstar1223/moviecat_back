package com.moviecat.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class MvcRcmdtnInfoDto {
        private long rcmdtnId; // 추천 id
        private long rcmdtnSeId; // 추천구분ID, 게시글인 경우 게시글ID,, 평점인 경우 평점ID
        private int rcmdtnSe; // 게시판, 평점 구분
        private String mbrId;
        private String mbrNm;
}
