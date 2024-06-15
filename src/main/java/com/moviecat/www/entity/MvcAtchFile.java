package com.moviecat.www.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
public class MvcAtchFile {

    @EmbeddedId
    private MvcAtchFilePK atchFileIdCk; // ID와 순서를 PK로 묶어서 받아온다.

    @Column(name = "actl_file_nm", nullable = false, length = 1036)
    private String actlFileNm; // 실제파일명(확장자 제외)

    @Column(name = "strg_file_nm", nullable = false, length = 1036)
    private String strgFileNm; // 저장파일명

    @Column(name = "strg_file_path", nullable = false, length = 1000)
    private String strgFilePath; // 저장파일경로(폴더명 까지만)

    @Column(name = "strg_file_size", nullable = false)
    private int strgFileSize; // 저장파일사이즈(바이트)

    @Column(name = "strg_file_extn", nullable = false, length = 30)
    private String strgFileExtn; // 저장파일확장자

    @Column(name = "rgst_user_id", nullable = false, length = 20)
    private String rgstUserId; // 등록ID

    @Column(name = "rgst_user_nm", nullable = false, length = 80)
    private String rgstUserNm; // 등록자

    @Column(name = "rgst_day", nullable = false)
    private Timestamp rgstDay; // 등록일

    @Column(name = "mdfcn_user_id", nullable = false, length = 20)
    private String mdfcnUserId; // 수정ID

    @Column(name = "mdfcn_user_nm", nullable = false, length = 80)
    private String mdfcnUserNm; // 수정자

    @Column(name = "mdfcn_day", nullable = false)
    private Timestamp mdfcnDay; // 수정일

    @Column(name = "delt_yn", nullable = false, length = 1)
    private String deltYn = "N"; // 삭제유무, 기본 "N"
}