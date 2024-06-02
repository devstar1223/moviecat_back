package com.moviecat.www.controller;

import com.moviecat.www.dto.MvcMbrInfoDto;
import com.moviecat.www.service.MvcMbrInfoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MbrController {

    private final MvcMbrInfoService mvcMbrInfoService;

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "회원가입 api")
    public ResponseEntity<String> registerMember(@RequestBody MvcMbrInfoDto mvcMbrInfoDto) {
        mvcMbrInfoService.registerMember(mvcMbrInfoDto);
        return new ResponseEntity<>("회원 가입 성공", HttpStatus.OK);
    }

    @PatchMapping("/editInfo")
    @Operation(summary = "회원 정보 수정", description = "mvcId로 식별, json으로 mvcId,nickNm,pswd,email,phoneNo,intrlntrcn,atchField 보내주면 됩니다.")
    public ResponseEntity<String> editMember(@RequestBody MvcMbrInfoDto mvcMbrInfoDto) {
        mvcMbrInfoService.editMember(mvcMbrInfoDto);
        return new ResponseEntity<>("정보 수정 성공", HttpStatus.OK);
    }
}