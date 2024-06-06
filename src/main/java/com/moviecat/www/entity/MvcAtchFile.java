package com.moviecat.www.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class MvcAtchFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "atch_file_id", nullable = true)
    private long atchFileid; // 첨부파일 id

    @Column(name = "pst_id", nullable = false)
    private long pstId; // 순서

    @Column(name = "ttl", nullable = false, length = 240)
    private String ttl; // 제목

    @Column(name = "cn", nullable = false, length = 5000)
    private String cn; // 내용

    @Column(name = "spo_yn", nullable = false, length = 1)
    private String spoYn = "N"; // 스포일러 유뮤, 기본 "N"

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
