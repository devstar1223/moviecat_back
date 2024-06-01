package com.moviecat.www.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf)-> csrf.disable()); // csrf 공격 방어 해제 (임시 해제, 배포시 활성화)
        http.authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/login").permitAll() // /login 페이지 인증없이 접근 허용
//                        .requestMatchers("/write").authenticated() // /write 경로에 대해 인증 요구(예시)
                        .anyRequest().permitAll() // 그 외의 모든 요청은 인증 없이 접근 허용


        ).formLogin(formLogin ->
                formLogin
                        .loginPage("/login") // 커스텀 로그인 페이지 URL (안쓰면 /login에서만 로그인)
                        .loginProcessingUrl("/login") // 로그인 처리 url(엔드포인트)
                        .usernameParameter("mbrId") // 커스텀 사용자명 파라미터 (기본 username)
                        .passwordParameter("pswd") // 커스텀 비밀번호 파라미터 (기본 password)
                        .defaultSuccessUrl("/success") // 로그인 성공 후 이동할 기본 URL
                        .failureUrl("/fail") // 로그인 실패시 이동할 url (안적어주면 /login?error로 이동함)
                        .permitAll()
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}