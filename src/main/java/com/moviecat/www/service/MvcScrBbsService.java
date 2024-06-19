package com.moviecat.www.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.dto.MvcScrBbsDto;
import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.entity.MvcRcmdtnInfo;
import com.moviecat.www.entity.MvcScrBbs;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.repository.MvcRcmdtnInfoRepository;
import com.moviecat.www.repository.MvcScrBbsRepository;
import com.moviecat.www.util.TimeFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MvcScrBbsService {

    private final MvcScrBbsRepository mvcScrBbsRepository;
    private final MvcRcmdtnInfoRepository mvcRcmdtnInfoRepository;
    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    private final TimeFormat timeFormat;

    @Transactional
    public void scrBbsWrite(MvcScrBbsDto mvcScrBbsDto){
        MvcScrBbs newScr = new MvcScrBbs();
        newScr.setMenuId(mvcScrBbsDto.getMenuId());
        newScr.setVdoNm(mvcScrBbsDto.getVdoNm());
        newScr.setOpngDay(mvcScrBbsDto.getOpngDay());
        newScr.setScr(mvcScrBbsDto.getScr());
        newScr.setVdoEvl(mvcScrBbsDto.getVdoEvl());
        newScr.setRgstUserId(mvcScrBbsDto.getMbrId());
        newScr.setRgstUserNm(mvcScrBbsDto.getMbrNm());
        newScr.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
        newScr.setMdfcnUserId(mvcScrBbsDto.getMbrId());
        newScr.setMdfcnUserNm(mvcScrBbsDto.getMbrNm());
        newScr.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
        newScr.setDeltYn("N");
        mvcScrBbsRepository.save(newScr);
    }

    @Transactional
    public String scrBbsRead(long scrId, String mbrId) throws JsonProcessingException {
        Optional<MvcScrBbs> scrOptional = mvcScrBbsRepository.findByScrIdAndDeltYn(scrId,"N");

        if(scrOptional.isPresent()){
            MvcScrBbs scr = scrOptional.get();
            Map<String, Object> scrMap = new LinkedHashMap<>();
            scrMap.put("scr", scr.getScr());
            scrMap.put("vdoNm", scr.getVdoNm());
            scrMap.put("vdoEvl", scr.getVdoEvl());

            String rgstTime = timeFormat.formatDate(scr.getRgstDay());
            scrMap.put("rgstDate", rgstTime);

            List<MvcRcmdtnInfo> rcmdList = mvcRcmdtnInfoRepository.findByRcmdtnSeIdAndMenuIdAndDeltYn(scr.getScrId(), scr.getMenuId(), "N"); // 해당되는 추천 리스트로 받아와서
            scrMap.put("rcmd", rcmdList.size()); // 사이즈 만큼 좋아요 수 할당

            Optional<MvcRcmdtnInfo> rcmdOptional = mvcRcmdtnInfoRepository.findByRgstUserIdAndDeltYn(mbrId, "N");
            if(rcmdOptional.isPresent()){
                scrMap.put("rcmdDeltYn","N");
            }
            else{
                scrMap.put("rcmdDeltYn","Y");
            }

            Optional<MvcMbrInfo> mbrInfoOptional = mvcMbrInfoRepository.findByRgstUserId(scr.getRgstUserId()); // 등록id로 유저 찾아오기
            MvcMbrInfo mbrInfo = mbrInfoOptional.get();
            scrMap.put("profileUrl", mbrInfo.getAtchFileUrl()); // url 찾아 넣기 (없으면 null 넣음)
            scrMap.put("nickNm", mbrInfo.getNickNm()); // nickNm 찾아 넣기

            // ObjectMapper를 사용하여 맵을 JSON으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(scrMap);
        }else {
            // Optional이 비어있는 경우 처리
            throw new NoSuchElementException("해당 게시글이 존재하지 않습니다.");
        }
    }

    public void scrBbsEdit(MvcScrBbsDto mvcScrBbsDto) {
        Optional<MvcScrBbs> scrOptional = mvcScrBbsRepository.findById(mvcScrBbsDto.getScrId());
        if(scrOptional.isPresent()){
            MvcScrBbs scr = scrOptional.get();
            scr.setScr(mvcScrBbsDto.getScr());
            scr.setVdoEvl(mvcScrBbsDto.getVdoEvl());
            scr.setMdfcnUserId(mvcScrBbsDto.getMbrId());
            scr.setMdfcnUserNm(mvcScrBbsDto.getMbrNm());
            scr.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
            mvcScrBbsRepository.save(scr);
        }
        else {
            throw new NoSuchElementException("해당 평점이 존재하지 않습니다.");
        }
    }

    public void scrBbsDelete(MvcScrBbsDto mvcScrBbsDto) {
        Optional<MvcScrBbs> scrOptional = mvcScrBbsRepository.findById(mvcScrBbsDto.getScrId());
        if(scrOptional.isPresent()){
            MvcScrBbs scr = scrOptional.get();
            scr.setDeltYn("Y");
            scr.setMdfcnUserId(mvcScrBbsDto.getMbrId());
            scr.setMdfcnUserNm(mvcScrBbsDto.getMbrNm());
            scr.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
            mvcScrBbsRepository.save(scr);
        }
        else {
            throw new NoSuchElementException("해당 평점이 존재하지 않습니다.");
        }
    }
}
