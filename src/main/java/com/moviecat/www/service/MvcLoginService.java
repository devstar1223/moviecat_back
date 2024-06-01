package com.moviecat.www.service;

import com.moviecat.www.repository.MvcMbrInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MvcLoginService implements UserDetailsService {

    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    @Override
    public UserDetails loadUserByUsername(String mbrId) throws UsernameNotFoundException {
        var result = mvcMbrInfoRepository.findByMbrId(mbrId);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("id가 존재하지 않습니다.");
        }
        var user = result.get();
        List<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority("일반유저")); // 우선, 모든 유저에게 일반유저 부여
        return new User(user.getMbrId(), user.getPswd(), auth); // 우선, ID PW 권한만 리턴함
    }
}
