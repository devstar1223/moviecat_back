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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MbrController {

    private final MvcMbrInfoService mvcMbrInfoService;

    @PostMapping("/join")
    @Operation(summary = "회원 가입", description = "회원가입 api")
    public ResponseEntity<String> joinMember(@ModelAttribute MvcMbrInfoDto mvcMbrInfoDto) {
        System.out.println("=== 가입 요청 ===");
        mvcMbrInfoService.joinMember(mvcMbrInfoDto);
        return new ResponseEntity<>("회원 가입 성공", HttpStatus.OK);
    }

    @PatchMapping("/editInfo")
    @Operation(summary = "회원 정보 수정", description = "mvcId로 식별, json으로 mvcId,nickNm,pswd,email,phoneNo,intrlntrcn,atchField 보내주면 됩니다.")
    public ResponseEntity<String> editMember(@RequestBody MvcMbrInfoDto mvcMbrInfoDto) {
        mvcMbrInfoService.editMember(mvcMbrInfoDto);
        return new ResponseEntity<>("정보 수정 성공", HttpStatus.OK);
    }

    @GetMapping("/mbrIdDupCheck")
    @Operation(summary = "ID 중복 체크", description = "중복시 409 CONFLICT 반환. 없을시 200 OK")
    public ResponseEntity<String> idDupCheck(@RequestParam String mbrId) {
        Boolean isDuplicate = mvcMbrInfoService.idDupCheck(mbrId);
        if (isDuplicate) {
            return new ResponseEntity<>("중복된 ID", HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>("사용 가능 ID", HttpStatus.OK);
        }
    }
    @GetMapping("/nickNmDupCheck")
    @Operation(summary = "닉네임 중복 체크", description = "중복시 409 CONFLICT 반환. 없을시 200 OK")
    public ResponseEntity<String> nickNmDupCheck(@RequestParam String nickNm) {
        Boolean isDuplicate = mvcMbrInfoService.nickNmDupCheck(nickNm);
        if (isDuplicate) {
            return new ResponseEntity<>("중복된 닉네임", HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>("사용 가능 닉네임", HttpStatus.OK);
        }
    }
}