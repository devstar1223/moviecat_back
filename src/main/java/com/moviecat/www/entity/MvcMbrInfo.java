package com.moviecat.www.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class MvcMbrInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mvc_id", nullable = false)
    private long mvcId; // 무비캣 ID

    @Column(name = "mbr_id", nullable = false, length = 20)
    private String mbrId; // 회원 ID (SNS 로그인의 경우 SNS에서 보내주는)

    @Column(name = "mbr_se", nullable = false)
    private int mbrSe; // 회원 구분 (카카오일 경우 1)

    @Column(name = "mbr_nm", nullable = false, length = 80)
    private String mbrNm; // 회원이름

    @Column(name = "nick_nm", nullable = false, length = 24)
    private String nickNm; // 닉네임

    @Column(name = "pswd", nullable = false, length = 1000)
    private String pswd; // 비밀번호, 암호화 때문에 길이 늘림

    @Column(name = "email", nullable = false, length = 40)
    private String email; // 이메일

    @Column(name = "phone_no", nullable = true, length = 11)
    private String phoneNo; // 휴대폰번호

    @Column(name = "intr_intrcn", nullable = true, length = 800)
    private String intrIntrcn; // 자기소개

    @Column(name = "atch_file_url", nullable = true)
    private String atchFileUrl; // 첨부파일 URL

    @Column(name = "trms_agre", nullable = false)
    private char trmsAgre = 'N'; // 이용약관동의 Y/N

    @Column(name = "info_agre", nullable = false)
    private char infoAgre = 'N'; // 개인정보수집동의 Y/N

    @Column(name = "mark_agre", nullable = false)
    private char markAgre = 'N'; // 마케팅목적동의 Y/N

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
    }
