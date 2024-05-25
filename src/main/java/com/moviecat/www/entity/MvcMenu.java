package com.moviecat.www.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class MvcMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id", nullable = false)
    private int menuId; // 메뉴 ID

    @Column(name = "menu_nm", nullable = false, length = 80)
    private String menuNm; // 메뉴명

    @Column(name = "menu_path", nullable = false, length = 1000)
    private String menuPath; // 메뉴경로

    @Column(name = "seq", nullable = false)
    private int seq; // 순서

    @Column(name = "expln", nullable = true, length = 400)
    private String expln; // 설명

    @Column(name = "rgst_user_id", nullable = false)
    private int rgstUserId; // 등록 ID

    @Column(name = "rgst_user_nm", nullable = false, length = 80)
    private String rgstUserNm; // 등록자

    @Column(name = "rgst_day", nullable = false)
    private Timestamp rgstDay; // 등록일 (년 월 일 시 분 초)

    @Column(name = "mdfcn_user_id", nullable = false)
    private int mdfcnUserId; // 수정 ID

    @Column(name = "mdfcn_user_nm", nullable = false, length = 80)
    private String mdfcnUserNm; // 수정자

    @Column(name = "mdfcn_day", nullable = false)
    private Timestamp mdfcnDay; // 수정일

    @Column(name = "use_yn", nullable = false, length = 1)
    private String useYn = "Y"; // 사용여부
}
