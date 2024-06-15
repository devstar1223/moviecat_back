package com.moviecat.www.service;

import com.moviecat.www.dto.MvcRcmdtnInfoDto;
import com.moviecat.www.entity.MvcRcmdtnInfo;
import com.moviecat.www.repository.MvcRcmdtnInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MvcRcmdtnInfoService {
    private final MvcRcmdtnInfoRepository mvcRcmdtnInfoRepository;
    public void recommend(MvcRcmdtnInfoDto mvcRcmdtnInfoDto){
        Optional<MvcRcmdtnInfo> rcmdOptional = mvcRcmdtnInfoRepository.findByRcmdtnSeAndRcmdtnSeIdAndRgstUserId(mvcRcmdtnInfoDto.getRcmdtnSe(),mvcRcmdtnInfoDto.getRcmdtnSeId(), mvcRcmdtnInfoDto.getMbrId());
        if(rcmdOptional.isPresent()){ // 추천 기록 있음
            MvcRcmdtnInfo rcmdInfo = rcmdOptional.get();
            if(rcmdInfo.getDeltYn().equals("Y")){
                rcmdInfo.setDeltYn("N");
            }
            else{
                rcmdInfo.setDeltYn("Y");
            }
            rcmdInfo.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
            mvcRcmdtnInfoRepository.save(rcmdInfo);
        }
        else{ // 추천 기록 없음
            MvcRcmdtnInfo newRcmd = new MvcRcmdtnInfo();
            newRcmd.setRcmdtnSeId(mvcRcmdtnInfoDto.getRcmdtnSeId());
            newRcmd.setRcmdtnSe(mvcRcmdtnInfoDto.getRcmdtnSe());
            newRcmd.setRgstUserId(mvcRcmdtnInfoDto.getMbrId());
            newRcmd.setRgstUserNm(mvcRcmdtnInfoDto.getMbrNm());
            newRcmd.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
            newRcmd.setMdfcnUserId(mvcRcmdtnInfoDto.getMbrId());
            newRcmd.setMdfcnUserNm(mvcRcmdtnInfoDto.getMbrNm());
            newRcmd.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
            newRcmd.setDeltYn("N"); //첫 추천시는 무조건 "N"
            mvcRcmdtnInfoRepository.save(newRcmd);
        }
    }
}
