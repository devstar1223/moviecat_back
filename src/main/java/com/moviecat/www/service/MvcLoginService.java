package com.moviecat.www.service;

import com.moviecat.www.util.JwtTokenProvider;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.util.MvcUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MvcLoginService implements UserDetailsService {

    private final MvcMbrInfoRepository mvcMbrInfoRepository;

    @Transactional
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
        MvcUserDetails userDetails = new MvcUserDetails(user.getMvcId(),user.getMbrId(), user.getPswd(), auth, user.getNickNm(), user.getMbrNm(), user.getAtchFileUrl(), token);
        return userDetails;
    }

}
