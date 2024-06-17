package com.moviecat.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MvcCmntDto {
    private long pstId;
    private String cmntMbrId;
    private String cmntMbrNickNm;
    private String cn;
}
