package com.moviecat.www.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.dto.MvcLoginDto;
import com.moviecat.www.dto.MvcMbrInfoDto;
import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.util.JwtTokenProvider;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MvcMbrInfoService {

    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    @Value("${kakao.key.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public void joinMember(MvcMbrInfoDto mvcMbrInfoDto) {
        MvcMbrInfo mvcMbrInfo = new MvcMbrInfo();
        mvcMbrInfo.setMbrId(mvcMbrInfoDto.getMbrId());
        mvcMbrInfo.setMbrSe(mvcMbrInfoDto.getMbrSe());
        mvcMbrInfo.setMbrNm(mvcMbrInfoDto.getMbrNm());
        mvcMbrInfo.setNickNm(mvcMbrInfoDto.getNickNm());
        mvcMbrInfo.setPswd(passwordEncoder.encode(mvcMbrInfoDto.getPswd()));
        mvcMbrInfo.setEmail(mvcMbrInfoDto.getEmail());
        mvcMbrInfo.setPhoneNo((mvcMbrInfoDto.getPhoneNo()).replaceAll("-", ""));
        mvcMbrInfo.setIntrIntrcn(mvcMbrInfoDto.getIntrIntrcn());
        mvcMbrInfo.setAtchFileUrl(mvcMbrInfoDto.getAtchFileUrl());
        mvcMbrInfo.setTrmsAgre(mvcMbrInfoDto.getTrmsAgre());
        mvcMbrInfo.setInfoAgre(mvcMbrInfoDto.getInfoAgre());
        mvcMbrInfo.setMarkAgre(mvcMbrInfoDto.getMarkAgre());
        mvcMbrInfo.setRgstUserId(mvcMbrInfoDto.getMbrId());
        mvcMbrInfo.setRgstUserNm(mvcMbrInfoDto.getMbrNm());
        mvcMbrInfo.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
        mvcMbrInfo.setMdfcnUserId(mvcMbrInfoDto.getMbrId());
        mvcMbrInfo.setMdfcnUserNm(mvcMbrInfoDto.getMbrNm());
        mvcMbrInfo.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
        mvcMbrInfoRepository.save(mvcMbrInfo);
    }

    public void editMember(MvcMbrInfoDto newMbrInfoDto) {
        Optional<MvcMbrInfo> mbrInfoOptional = mvcMbrInfoRepository.findById(newMbrInfoDto.getMvcId()); // 받아온 정보로 회원 찾기
        MvcMbrInfo mbrInfo = mbrInfoOptional.get();
        mbrInfo.setNickNm(newMbrInfoDto.getNickNm());
        mbrInfo.setPswd(passwordEncoder.encode(newMbrInfoDto.getPswd()));
        mbrInfo.setEmail(newMbrInfoDto.getEmail());
        mbrInfo.setPhoneNo(newMbrInfoDto.getPhoneNo());
        mbrInfo.setIntrIntrcn(newMbrInfoDto.getIntrIntrcn());
        mbrInfo.setAtchFileUrl(newMbrInfoDto.getAtchFileUrl());
        mbrInfo.setMdfcnUserId(mbrInfo.getMbrId()); // 수정 id는 현재 id를 등록 (id는 변경 불가) (admin이 재설정은 다른 api)
        mbrInfo.setMdfcnUserNm(newMbrInfoDto.getNickNm()); // 수정 닉네임은 바꾼 닉네임을 등록 (admin이 재설정은 다른 api)
        mbrInfo.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 수정 날짜 현재로 등록
        mvcMbrInfoRepository.save(mbrInfo);
    }

    public boolean idDupCheck(String mbrId){
        Optional<MvcMbrInfo> mbrIdOptional = mvcMbrInfoRepository.findByMbrId(mbrId); // 받아온 정보로 mbrId 찾기
        return mbrIdOptional.isPresent();
    }

    public boolean nickNmDupCheck(String nickNm){
        Optional<MvcMbrInfo> nickNmOptional = mvcMbrInfoRepository.findByNickNm(nickNm); // 받아온 정보로 nickNm 찾기
        return nickNmOptional.isPresent();
    }

    public String findId(String mbrNm, String email) {
        Optional<MvcMbrInfo> idOptional = mvcMbrInfoRepository.findByMbrNmAndEmail(mbrNm, email); // ID 있는지 확인
        if (idOptional.isPresent()) {
            return idOptional.get().getMbrId();
        } else {
            return null; // ID가 존재하지 않음을 나타내는 적절한 값으로 반환
        }
    }

    //snsLogin 처리
    public MvcLoginDto snsLogin(String code) {
        //1.인가 코드로 토큰 요청
        String accessToken = getAccessToken(code);

        //2. 토큰으로 카카오 API 호출(회원정보 호출)
        HashMap<String, Object> userInfo = getKakaoUserInfo(accessToken);

        //3. 카카오ID로 회원가입 & 로그인 처리
        MvcLoginDto mvcLoginDto = kakaoUserLogin(userInfo);

        return mvcLoginDto;
    }

    //토큰 요청
    private String getAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonNode.get("access_token").asText();
    }

    //회원 정보 요청
    private HashMap<String, Object> getKakaoUserInfo(String accessToken) {

        HashMap<String, Object> userInfo= new HashMap<String,Object>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Long mvcId = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickNm = jsonNode.get("properties").get("nickname").asText();
        String atchFileUrl = Optional.ofNullable(jsonNode)
                .filter(node -> node.has("properties") && node.get("properties").has("profile_image_url"))
                .map(node -> node.get("properties").get("profile_image_url").asText())
                .orElse("");

        userInfo.put("mvcId",mvcId);
        userInfo.put("email",email);
        userInfo.put("nickNm",nickNm);
        userInfo.put("atchFileUrl",atchFileUrl);

        return userInfo;
    }

    //카카오 로그인 및 회원가입
    private MvcLoginDto kakaoUserLogin(HashMap<String, Object> userInfo){

        Long mvcId= Long.valueOf(userInfo.get("mvcId").toString()); // 카톡에서 보내주는 아이디
        String email = userInfo.get("email").toString(); // 이메일
        String nickNm = userInfo.get("nickNm").toString(); // 닉네임
        String atchFileUrl = userInfo.get("atchFileUrl").toString(); // 프로필사진                                ----------------------------------------------------------*********************************************************************************************************************************************************************************************************************************************************************************/

        //TODO.강산님 회원가입 처리할 곳(아이디가 db에 존재하지 않을때만 회원가입)
        //아직 테스트 해보지 못했습니다.
        String kakaoIdPlusK = "K"+mvcId;
        Optional<MvcMbrInfo> kakaoIdOptional = mvcMbrInfoRepository.findByMbrId(kakaoIdPlusK);
        if(!kakaoIdOptional.isPresent()){
            MvcMbrInfo kakaoJoinInfo = kakaoIdOptional.get();
            MvcMbrInfo kakaoMbr = new MvcMbrInfo();
            kakaoMbr.setMbrId("K" + kakaoJoinInfo.getMvcId());
            kakaoMbr.setEmail(kakaoJoinInfo.getEmail());
            kakaoMbr.setNickNm(kakaoJoinInfo.getNickNm());
            kakaoMbr.setAtchFileUrl(kakaoJoinInfo.getAtchFileUrl());
            kakaoMbr.setMbrSe(1); // 카카오는 1
            kakaoMbr.setMbrNm(""); // 이름칸 비워둠
            kakaoMbr.setPswd(""); // 카카오 로그인 비밀번호 X
            kakaoMbr.setTrmsAgre('Y');
            kakaoMbr.setInfoAgre('Y');
            kakaoMbr.setMarkAgre('N');
            kakaoMbr.setRgstUserId("K" + kakaoJoinInfo.getMvcId());
            kakaoMbr.setRgstUserNm(""); // 이름칸 비워져있기 때문에
            kakaoMbr.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
            kakaoMbr.setMdfcnUserId("K" + kakaoJoinInfo.getMvcId());
            kakaoMbr.setMdfcnUserNm(""); // 이름칸 비워져있기 때문에
            kakaoMbr.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
            mvcMbrInfoRepository.save(kakaoMbr);
        }
        return new MvcLoginDto(mvcId, nickNm, email, atchFileUrl, jwtTokenProvider.generateToken(mvcId.toString()));
    }
}
