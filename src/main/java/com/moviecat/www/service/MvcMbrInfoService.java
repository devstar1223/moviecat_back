package com.moviecat.www.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.dto.MvcLoginDto;
import com.moviecat.www.dto.MvcMbrInfoDto;
import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.util.JwtTokenProvider;
import com.moviecat.www.util.PasswordGenerator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.mail.MailSender;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MvcMbrInfoService {
    
    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
    private final MailSender mailSender;
    private final MvcProfileImageService mvcProfileImageService;

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
        mvcMbrInfo.setTrmsAgre(mvcMbrInfoDto.getTrmsAgre());
        mvcMbrInfo.setInfoAgre(mvcMbrInfoDto.getInfoAgre());
        mvcMbrInfo.setMarkAgre(mvcMbrInfoDto.getMarkAgre());
        mvcMbrInfo.setRgstUserId(mvcMbrInfoDto.getMbrId());
        mvcMbrInfo.setRgstUserNm(mvcMbrInfoDto.getMbrNm());
        mvcMbrInfo.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
        mvcMbrInfo.setMdfcnUserId(mvcMbrInfoDto.getMbrId());
        mvcMbrInfo.setMdfcnUserNm(mvcMbrInfoDto.getMbrNm());
        mvcMbrInfo.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
        if(mvcMbrInfoDto.getProfileImage() != null){
            String path = mvcProfileImageService.uploadFile(mvcMbrInfoDto.getProfileImage());
            mvcMbrInfo.setAtchFileUrl(path);
        }
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
            String foundId = idOptional.get().getMbrId();
            return foundId.substring(0, foundId.length() - 3) + "***";
        } else {
            return null;
        }
    }

    public boolean findPswd(String mbrId, String mbrNm, String email) {
        Optional<MvcMbrInfo> pswdOptional = mvcMbrInfoRepository.findByMbrIdAndMbrNmAndEmail(mbrId, mbrNm, email);; // ID 있는지 확인
        if (pswdOptional.isPresent()) {
            MvcMbrInfo foundPswdMbr = pswdOptional.get();
            String newPswd = PasswordGenerator.generatePassword(10);
            foundPswdMbr.setPswd(passwordEncoder.encode(newPswd));
            foundPswdMbr.setMdfcnUserId("admin"); // admin으로 수정 ID 변경
            foundPswdMbr.setMdfcnUserNm("운영자"); // 운영자로 수정자 변경
            foundPswdMbr.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 수정 날짜 현재로 등록
            mvcMbrInfoRepository.save(foundPswdMbr);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("devstar1223@gmail.com");
            message.setTo(email);
            message.setReplyTo("devstar1223@gmail.com");
            message.setSubject("이메일 제목");
            message.setText("본문 내용 : " + newPswd + " 입니다.");
            mailSender.send(message);
            return true;
        } else {
            return false;
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
        String phoneNo = Optional.ofNullable(jsonNode)
                .map(node -> node.get("kakao_account"))
                .filter(node -> node.has("has_phone_number") && node.get("has_phone_number").asBoolean())
                .map(node -> node.get("phone_number"))
                .map(JsonNode::asText)
                .orElse("");
        String mbrNm = jsonNode.get("kakao_account").get("name").asText();
        String nickNm = jsonNode.get("properties").get("nickname").asText();
        String atchFileUrl = Optional.ofNullable(jsonNode)
                .filter(node -> node.has("properties") && node.get("properties").has("profile_image_url"))
                .map(node -> node.get("properties").get("profile_image_url").asText())
                .orElse("");

        userInfo.put("mvcId",mvcId);
        userInfo.put("mbrNm",mbrNm);
        userInfo.put("phoneNo",phoneNo);
        userInfo.put("email",email);
        userInfo.put("nickNm",nickNm);
        userInfo.put("atchFileUrl",atchFileUrl);

        return userInfo;
    }

    //카카오 로그인 및 회원가입
    private MvcLoginDto kakaoUserLogin(HashMap<String, Object> userInfo){

        Long mvcId= Long.valueOf(userInfo.get("mvcId").toString());
        String email = userInfo.get("email").toString();
        String nickNm = userInfo.get("nickNm").toString();
        String mbrNm = userInfo.get("mbrNm").toString();
        String phoneNo = userInfo.get("phoneNo").toString();
        String atchFileUrl = userInfo.get("atchFileUrl").toString();

        //TODO.강산님 회원가입 처리할 곳(아이디가 db에 존재하지 않을때만 회원가입)
        //TODO.에러나서 우선 주석처리했습니다.
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
            kakaoMbr.setPhoneNo(kakaoJoinInfo.getPhoneNo()); // 안들어오거나, 혹시 -가 붙여서 들어오진 않을까..
            kakaoMbr.setMbrSe(1); // 카카오는 1
            kakaoMbr.setMbrNm(kakaoJoinInfo.getMbrNm());
            kakaoMbr.setPswd(""); // 카카오 로그인 비밀번호 X
            kakaoMbr.setTrmsAgre('N');
            kakaoMbr.setInfoAgre('N');
            kakaoMbr.setMarkAgre('N');
            kakaoMbr.setRgstUserId("K" + kakaoJoinInfo.getMvcId());
            kakaoMbr.setRgstUserNm(kakaoJoinInfo.getMbrNm());
            kakaoMbr.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
            kakaoMbr.setMdfcnUserId("K" + kakaoJoinInfo.getMvcId());
            kakaoMbr.setMdfcnUserNm(kakaoJoinInfo.getMbrNm());
            kakaoMbr.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
            mvcMbrInfoRepository.save(kakaoMbr);
        }
        return new MvcLoginDto(mvcId, nickNm, mbrNm, email, atchFileUrl, jwtTokenProvider.generateToken(mvcId.toString()));
    }
}
