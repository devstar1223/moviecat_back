package com.moviecat.www.util;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class MvcUserDetails extends User {

    private final String nickNm;
    private final String token;

    public MvcUserDetails(String mbrId, String pswd, Collection<? extends GrantedAuthority> auth, String nickNm, String token) {
        super(mbrId, pswd, auth);
        this.nickNm = nickNm;
        this.token = token;
    }
}
