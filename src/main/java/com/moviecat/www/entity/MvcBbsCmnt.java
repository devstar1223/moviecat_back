package com.moviecat.www.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class MvcBbsCmnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cmnt_id", nullable = false)
    private long cmntId; // 댓글 ID

    @Column(name = "pst_id", nullable = false)
    private long pstId; // 게시글 ID

    @Column(name = "up_cmnt_id", nullable = false)
    private int upCmntId; // 상위 댓글 ID , 대댓글은 상위 댓글 ID

    @Column(name = "cmnt_lyr", nullable = false)
    private int cmntLyr; // 댓글 계층, 댓글은 0, 대댓글은 1, 대댓글에 대한 댓글은 2

    @Column(name = "cmnt_group", nullable = false)
    private int cmntGroup; // 댓글 그룹, 댓글 그룹, 순서대로 0부터 부여

    @Column(name = "cmnt_mbr_id", nullable = false, length = 100)
    private String cmntMbrId; // 댓글 멤버 ID, 대댓글에 대한 댓글인 경우 댓글 멤버의 ID

    @Column(name = "cmnt_mbr_nick_nm", nullable = false, length = 80)
    private String cmntMbrNickNm; // 댓글 멤버 닉네임, 대댓글에 대한 댓글인 경우 댓글 멤버의 NICKNAME

    @Column(name = "seq", nullable = false)
    private int seq; // 순서

    @Column(name = "cn", nullable = false, length = 1200)
    private String cn; // 내용

    @Column(name = "rgst_user_id", nullable = false, length = 20)
    private String rgstUserId; // 등록자 ID

    @Column(name = "rgst_user_nm", nullable = false, length = 80)
    private String rgstUserNm; // 등록자

    @Column(name = "rgst_day", nullable = false)
    private Timestamp rgstDay; // 등록일

    @Column(name = "mdfcn_user_id", nullable = false, length = 20)
    private String mdfcnUserId; // 수정자 ID

    @Column(name = "mdfcn_user_nm", nullable = false, length = 80)
    private String mdfcnUserNm; // 수정자

    @Column(name = "mdfcn_day", nullable = false)
    private Timestamp mdfcnDay; // 수정일

    @Column(name = "delt_yn", nullable = false)
    private char deltYn = 'N'; // 삭제 여부
}