package com.moviecat.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MvcCmntDto {
    private long cmtnId; // 댓글 쓰기, 답글 쓰기에는 필요없음.
    private long pstId;
    private String cmntMbrId;
    private String cmntMbrNickNm;
    private String cn;
    private long upCmntId; // 댓글 쓰기, 수정에는 필요없음.
}
