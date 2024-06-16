package com.moviecat.www.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class MvcRcmdtnInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rcmdtn_id", nullable = false)
    private long rcmdtnId; // 추천ID

    @Column(name = "rcmdtn_se_id", nullable = false)
    private long rcmdtnSeId; // 추천구분ID, 게시글인 경우 게시글ID,, 평점인 경우 평점ID

    @Column(name = "rcmdtn_se", nullable = false)
    private int rcmdtnSe; // 게시판, 평점 구분(게시판 각각 1 2 3 평점 4)

    @Column(name = "rgst_user_id", nullable = false, length = 20)
    private String rgstUserId; // 등록 ID

    @Column(name = "rgst_user_nm", nullable = false, length = 80)
    private String rgstUserNm; // 등록자

    @Column(name = "rgst_day", nullable = false)
    private Timestamp rgstDay; // 등록일 (년 월 일 시 분 초)

    @Column(name = "mdfcn_user_id", nullable = false, length = 20)
    private String mdfcnUserId; // 수정 ID

    @Column(name = "mdfcn_user_nm", nullable = false, length = 80)
    private String mdfcnUserNm; // 수정자

    @Column(name = "mdfcn_day", nullable = false)
    private Timestamp mdfcnDay; // 수정일

    @Column(name = "delt_yn", nullable = false, length = 1)
    private String deltYn = "N"; // 삭제유무, 기본 "N"
}
