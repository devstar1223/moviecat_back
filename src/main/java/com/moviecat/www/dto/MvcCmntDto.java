package com.moviecat.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MvcCmntDto {
    private Long cmntId; // 댓글 쓰기, 답글 쓰기에는 필요없음.
    private Long pstId;
    private String cmntMbrId;
    private String cmntMbrNickNm;
    private String cn; // 댓글 삭제시에는 필요없음.
    private Long upCmntId; // 댓글 쓰기, 수정, 삭제에는 필요없음.
    private String mbrId;
    private String mbrNm;
}
