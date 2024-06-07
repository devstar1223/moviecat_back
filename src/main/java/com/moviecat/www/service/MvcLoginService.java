package com.moviecat.www.service;

import com.moviecat.www.util.JwtTokenProvider;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.util.MvcUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MvcLoginService implements UserDetailsService {

    private final MvcMbrInfoRepository mvcMbrInfoRepository;

    // 보안을 위한 시크릿 키
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    // 토큰 유효 시간 (30분)
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    @Override
    public UserDetails loadUserByUsername(String mbrId) throws UsernameNotFoundException {
        var result = mvcMbrInfoRepository.findByMbrId(mbrId);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("id가 존재하지 않습니다.");
        }
        var user = result.get();
        List<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority("일반유저")); // 우선, 모든 유저에게 일반유저 부여
        String token = JwtTokenProvider.generateToken(user.getMbrId());
        MvcUserDetails userDetails = new MvcUserDetails(user.getMbrId(), user.getNickNm(), user.getAtchFileUrl(), auth, token);
        return userDetails;
    }

    // 토큰 생성
    private String generateToken(String mbrId) {
        return Jwts.builder()
                .setSubject(mbrId)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }
}
