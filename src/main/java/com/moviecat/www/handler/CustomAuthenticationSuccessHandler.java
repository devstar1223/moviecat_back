package com.moviecat.www.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.util.MvcUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_OK); // 성공 상태 코드 200 반환
        MvcUserDetails userDetails = (MvcUserDetails) authentication.getPrincipal();

        Map<String, Object> data = new HashMap<>();
        data.put("mbrId", userDetails.getUsername());
        data.put("nickNm", userDetails.getNickNm());
        data.put("atchFileUrl", userDetails.getAtchFileUrl());
        data.put("token", userDetails.getToken());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }
}
