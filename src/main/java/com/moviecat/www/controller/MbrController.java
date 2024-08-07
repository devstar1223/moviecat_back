package com.moviecat.www.controller;

import com.moviecat.www.dto.MvcLoginDto;
import com.moviecat.www.dto.MvcMbrInfoDto;
import com.moviecat.www.service.MvcMbrInfoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MbrController {

    private final MvcMbrInfoService mvcMbrInfoService;

    @PostMapping("/join")
    @Operation(summary = "회원 가입", description = "회원가입 api, 프로필 사진도 함께 받음.")
    public ResponseEntity<String> joinMember(@ModelAttribute MvcMbrInfoDto mvcMbrInfoDto, @RequestPart(required = false) MultipartFile profileImage) {
        mvcMbrInfoDto.setProfileImage(profileImage);
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

    @GetMapping("/findId")
    @Operation(summary = "아이디 찾기", description = "없을경우 404 NOTFOUND 반환. 있을시 200 OK와 ID 반환")
    public ResponseEntity<Object> findId(@RequestParam String mbrNm,@RequestParam String email) {
        String findId = mvcMbrInfoService.findId(mbrNm, email);
        if (findId == null) {
            return new ResponseEntity<>("해당 ID 없음", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(findId, HttpStatus.OK);
        }
    }

    @GetMapping("/findPswd")
    @Operation(summary = "비밀번호 찾기", description = "없을경우 404 NOTFOUND 반환. 있을시 200 OK와 임시 비밀번호 이메일 전송")
    public ResponseEntity<Object> findPswd(@ModelAttribute MvcMbrInfoDto mvcMbrInfoDto) {
        Boolean findPswd = mvcMbrInfoService.findPswd(mvcMbrInfoDto.getMbrId(), mvcMbrInfoDto.getMbrNm(), mvcMbrInfoDto.getEmail());
        if(findPswd){
            return new ResponseEntity<>("메일로 전송 완료", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("일치하는 계정 없음", HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/myInfoRead")
    @Operation(summary = "회원 정보", description ="회원 정보 조회")
    public ResponseEntity<String> myInfoRead(@RequestParam long mvcId){
        try {
            String jsonMyInfo = mvcMbrInfoService.myInfoRead(mvcId);
            return new ResponseEntity<>(jsonMyInfo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/myInfoUpdate")
    @Operation(summary = "회원 정보 수정", description ="일반회원 - mvcId, profileImg, pssword, nickNm, phoneNo, intrIntcn/SNS회원 - mvcId, phoneNo, intrIntcn")
    public ResponseEntity<String> myInfoUpdate(@ModelAttribute MvcMbrInfoDto mvcMbrInfoDto
            , @RequestPart(required = false) MultipartFile profileImage){

        try {

            mvcMbrInfoService.myInfoUpdate(mvcMbrInfoDto, profileImage);
            return new ResponseEntity<>("수정 완료", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/myInfoDelete")
    @Operation(summary = "회원 탈퇴", description ="deltYn 변경")
    public ResponseEntity<String> myInfoDelete(@RequestParam long mvcId){
        try {
            mvcMbrInfoService.myInfoDelete(mvcId);
            return new ResponseEntity<>("탈퇴 완료", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}