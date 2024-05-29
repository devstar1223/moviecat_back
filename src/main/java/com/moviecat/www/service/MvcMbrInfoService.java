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

@Service
@RequiredArgsConstructor
public class MvcMbrInfoService {

    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerMember(MvcMbrInfoDto mvcMbrInfoDto) {
        MvcMbrInfo mvcMbrInfo = new MvcMbrInfo();
        mvcMbrInfo.setMbrId(mvcMbrInfoDto.getMbrId());
        mvcMbrInfo.setMbrSe(mvcMbrInfoDto.getMbrSe());
        mvcMbrInfo.setMbrNm(mvcMbrInfoDto.getMbrNm());
        mvcMbrInfo.setNickNm(mvcMbrInfoDto.getNickNm());
        mvcMbrInfo.setPswd(passwordEncoder.encode(mvcMbrInfoDto.getPswd()));
        mvcMbrInfo.setEmail(mvcMbrInfoDto.getEmail());
        mvcMbrInfo.setPhoneNo(mvcMbrInfoDto.getPhoneNo());
        mvcMbrInfo.setIntrIntrcn(mvcMbrInfoDto.getIntrIntrcn());
        mvcMbrInfo.setAtchFileId(mvcMbrInfoDto.getAtchFileId());
        mvcMbrInfo.setTrmsAgre(mvcMbrInfoDto.getTrmsAgre());
        mvcMbrInfo.setInfoAgre(mvcMbrInfoDto.getInfoAgre());
        mvcMbrInfo.setMarkAgre(mvcMbrInfoDto.getMarkAgre());
        mvcMbrInfo.setRgstUserId(mvcMbrInfoDto.getRgstUserId());
        mvcMbrInfo.setRgstUserNm(mvcMbrInfoDto.getRgstUserNm());
        mvcMbrInfo.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
        mvcMbrInfo.setMdfcnUserId(mvcMbrInfoDto.getMdfcnUserId());
        mvcMbrInfo.setMdfcnUserNm(mvcMbrInfoDto.getMdfcnUserNm());
        mvcMbrInfo.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
        mvcMbrInfoRepository.save(mvcMbrInfo);
    }
}
