package com.moviecat.www.controller;

import com.moviecat.www.dto.MvcMbrInfoDto;
import com.moviecat.www.service.MvcMbrInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MbrController {

    private final MvcMbrInfoService mvcMbrInfoService;


    @PostMapping("/register")
    public ResponseEntity<String> registerMember(@RequestBody MvcMbrInfoDto mvcMbrInfoDto) {
        mvcMbrInfoService.registerMember(mvcMbrInfoDto);
        return new ResponseEntity<>("Member registered successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public String login(@RequestBody MvcMbrInfoDto mvcMbrInfoDto) {

        String mbrId = mvcMbrInfoDto.getMbrId();
        String pswd = mvcMbrInfoDto.getPswd();

        System.out.println(mbrId + " " + pswd);
        return mbrId + " " + pswd;
    }
}