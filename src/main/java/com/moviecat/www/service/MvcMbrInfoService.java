package com.moviecat.www.service;

import com.moviecat.www.dto.MvcMbrInfoDto;
import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MvcMbrInfoService {

    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    private final PasswordEncoder passwordEncoder;

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
}
