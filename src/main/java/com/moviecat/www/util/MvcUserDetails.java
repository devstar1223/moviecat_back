package com.moviecat.www.util;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class MvcUserDetails extends User {

    private final String nickNm;
    private final String atchFileUrl;
    private final String token;

    public MvcUserDetails(String username, String nickNm, String atchFileUrl, Collection<? extends GrantedAuthority> authorities, String token) {
        super(username, "", authorities);
        this.nickNm = nickNm;
        this.atchFileUrl = atchFileUrl;
        this.token = token;
    }
}
