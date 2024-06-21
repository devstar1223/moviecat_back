package com.moviecat.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MvcCmntDto {
    private Long cmntId; // 댓글 쓰기, 답글 쓰기에는 필요없음.
    private Long pstId; // 게시글 id
    private String cmntMbrId; // 댓글 멤버 ID, 대댓글에 대한 댓글인 경우 댓글 멤버의 ID
    private String cmntMbrNickNm; // 댓글 멤버 닉네임, 대댓글에 대한 댓글인 경우 댓글 멤버의 NICKNAME
    private String cn; // 댓글 삭제시에는 필요없음.
    private Long upCmntId; // 댓글 쓰기, 수정, 삭제에는 필요없음.
    private String mbrId; // 작성자 id , 닉네임 바뀌니까 쓰는거, 제발 외우기
    private String mbrNm; // 작성자 이름, 필요함.
    private int cmntLyr; // 댓글 계층
    private int cmntGroup; // 댓글 그룹
    private int seq; // 댓글 순서
}
