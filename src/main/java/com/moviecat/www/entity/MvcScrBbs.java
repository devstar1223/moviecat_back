package com.moviecat.www.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class MvcScrBbs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scr_id", nullable = false)
    private long scrId; // 평점 ID

    @Column(name = "vdo_nm", nullable = false, length = 200)
    private String vdoNm; // 영화명

    @Column(name = "opng_day", nullable = false)
    private Timestamp opngDay; // 개봉일

    @Column(name = "scr", nullable = false)
    private int scr; // 평점

    @Column(name = "vdo_evl", nullable = false, length = 240)
    private String vdoEvl; // 영화평

    @Column(name = "rgst_user_id", nullable = false, length = 20)
    private String rgstUserId; // 등록자 ID

    @Column(name = "rgst_user_nm", nullable = false, length = 80)
    private String rgstUserNm; // 등록자 이름

    @Column(name = "rgst_day", nullable = false)
    private Timestamp rgstDay; // 등록일

    @Column(name = "mdfcn_user_id", nullable = false, length = 20)
    private String mdfcnUserId; // 수정자 ID

    @Column(name = "mdfcn_user_nm", nullable = false, length = 80)
    private String mdfcnUserNm; // 수정자 이름

    @Column(name = "mdfcn_day", nullable = false)
    private Timestamp mdfcnDay; // 수정일

    @Column(name = "delt_yn", nullable = false, length = 1)
    private String deltYn = "N"; // 삭제유무, 기본 "N"

    @Column(name = "menu_id", nullable = false)
    private int menuId; // 메뉴 ID
}