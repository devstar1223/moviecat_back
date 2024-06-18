package com.moviecat.www.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.dto.MvcRcmdtnInfoDto;
import com.moviecat.www.entity.MvcRcmdtnInfo;
import com.moviecat.www.repository.MvcRcmdtnInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MvcRcmdtnInfoService {
    private final MvcRcmdtnInfoRepository mvcRcmdtnInfoRepository;
    private final ObjectMapper objectMapper;
    public String recommend(MvcRcmdtnInfoDto mvcRcmdtnInfoDto) throws JsonProcessingException {
        Map<String, Object> recommendMap = new LinkedHashMap<>();
        Optional<MvcRcmdtnInfo> rcmdOptional = mvcRcmdtnInfoRepository.findByMenuIdAndRcmdtnSeIdAndRgstUserId(mvcRcmdtnInfoDto.getMenuId(), mvcRcmdtnInfoDto.getRcmdtnSeId(), mvcRcmdtnInfoDto.getMbrId());
        if (rcmdOptional.isPresent()) { // 추천 기록 있음
            MvcRcmdtnInfo rcmdInfo = rcmdOptional.get();
            if (rcmdInfo.getDeltYn().equals("Y")) {
                rcmdInfo.setDeltYn("N");
            } else {
                rcmdInfo.setDeltYn("Y");
            }
            rcmdInfo.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
            mvcRcmdtnInfoRepository.save(rcmdInfo);
            recommendMap.put("deltYn", rcmdInfo.getDeltYn());
        } else { // 추천 기록 없음
            MvcRcmdtnInfo newRcmd = new MvcRcmdtnInfo();
            newRcmd.setRcmdtnSeId(mvcRcmdtnInfoDto.getRcmdtnSeId());
            newRcmd.setMenuId(mvcRcmdtnInfoDto.getMenuId());
            newRcmd.setRgstUserId(mvcRcmdtnInfoDto.getMbrId());
            newRcmd.setRgstUserNm(mvcRcmdtnInfoDto.getMbrNm());
            newRcmd.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
            newRcmd.setMdfcnUserId(mvcRcmdtnInfoDto.getMbrId());
            newRcmd.setMdfcnUserNm(mvcRcmdtnInfoDto.getMbrNm());
            newRcmd.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
            newRcmd.setDeltYn("N"); //첫 추천시는 무조건 "N"
            mvcRcmdtnInfoRepository.save(newRcmd);
            recommendMap.put("deltYn", "N");
        }
        List<MvcRcmdtnInfo> totalRcmdList = mvcRcmdtnInfoRepository.findByDeltYnAndMenuIdAndRcmdtnSeId("N", mvcRcmdtnInfoDto.getMenuId(), mvcRcmdtnInfoDto.getRcmdtnSeId());
        recommendMap.put("total", totalRcmdList.size());

        return objectMapper.writeValueAsString(recommendMap);
    }
}
