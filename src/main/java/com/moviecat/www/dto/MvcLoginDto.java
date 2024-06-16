package com.moviecat.www.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MvcLoginDto {
    private long mvcId;
    private String mbrId;
    private String nickNm;
    private String mbrNm;
    private String email;
    private String atchFileUrl;
    private String token;
}
