package com.moviecat.www.util;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class MvcUserDetails extends User {

    private final String mbrId;
    private final String nickNm;
    private final String mbrNm;
    private final String atchFileUrl;
    private final String token;
    private final long mvcId;

    public MvcUserDetails(long mvcId,String mbrId, String pswd, Collection<? extends GrantedAuthority> auth, String nickNm, String mbrNm, String atchFileUrl, String token) {
        super(mbrId, pswd, auth);
        this.mvcId = mvcId;
        this.mbrId = mbrId;
        this.nickNm = nickNm;
        this.mbrNm = mbrNm;
        this.token = token;
        this.atchFileUrl = atchFileUrl;
    }
}
