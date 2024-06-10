package com.moviecat.www.controller;

import com.moviecat.www.dto.MvcLoginDto;
import com.moviecat.www.dto.MvcMbrInfoDto;
import com.moviecat.www.service.MvcMbrInfoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class MbrController {

    private final MvcMbrInfoService mvcMbrInfoService;

    @PostMapping("/join")
    @Operation(summary = "회원 가입", description = "회원가입 api")
    public ResponseEntity<String> joinMember(@ModelAttribute MvcMbrInfoDto mvcMbrInfoDto) {
        mvcMbrInfoService.joinMember(mvcMbrInfoDto);
        return new ResponseEntity<>("회원 가입 성공", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/snsLogin")
    @Operation(summary = "sns 로그인 및 회원가입", description ="kakao 로그인 및 회원가입 처리")
    public ResponseEntity<MvcLoginDto> snsLogin(@RequestParam String code){
        try{
            return ResponseEntity.ok(mvcMbrInfoService.snsLogin(code));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
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

    @GetMapping("/idFind")
    @Operation(summary = "아이디 찾기", description = "없을경우 404 NOTFOUND 반환. 있을시 200 OK와 ID 반환")
    public ResponseEntity<Object> idFind(@RequestParam String mbrNm,@RequestParam String email) {
        String findId = mvcMbrInfoService.findId(mbrNm, email);
        if (findId == null) {
            return new ResponseEntity<>("해당 ID 없음", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(findId, HttpStatus.OK);
        }
    }

    @GetMapping("/pswdFind")
    @Operation(summary = "비밀번호 찾기", description = "없을경우 404 NOTFOUND 반환. 있을시 200 OK와 임시 비밀번호 이메일 전송")
    public ResponseEntity<Object> pswdFind(@ModelAttribute MvcMbrInfoDto mvcMbrInfoDto) {
        Boolean findPswd = mvcMbrInfoService.findPswd(mvcMbrInfoDto.getMbrId(), mvcMbrInfoDto.getMbrNm(), mvcMbrInfoDto.getEmail());
        if(findPswd){
            return new ResponseEntity<>("메일로 전송 완료", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("일치하는 계정 없음", HttpStatus.NOT_FOUND);
        }

    }
}