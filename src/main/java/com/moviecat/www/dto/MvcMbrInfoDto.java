package com.moviecat.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.MapUtils;

import java.sql.Timestamp;

@Getter
@Setter
public class MvcMbrInfoDto {
    private long mvcId; // 무비캣 ID
    private String mbrId; // 회원 ID (SNS 로그인의 경우 SNS에서 보내주는)
    private int mbrSe; // 회원 구분 (일반 0 카카오1)
    private String mbrNm; // 회원이름
    private String nickNm; // 닉네임
    private String pswd; // 비밀번호
    private String email; // 이메일
    private String phoneNo; // 휴대폰번호
    private String intrIntrcn; // 자기소개
    private MultipartFile profileImage; //첨부파일
    private String atchFileUrl; // 첨부파일 url
    private char trmsAgre; // 이용약관동의 Y/N
    private char infoAgre; // 개인정보수집동의 Y/N
    private char markAgre; // 마케팅목적동의 Y/N
    private String rgstUserId; // 등록 ID
    private String rgstUserNm; // 등록자
    private Timestamp rgstDay; // 등록일 (년 월 일 시 분 초)
    private String mdfcnUserId; // 수정 ID
    private String mdfcnUserNm; // 수정자
    private Timestamp mdfcnDay; // 수정일
}
