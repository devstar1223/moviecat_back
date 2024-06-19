package com.moviecat.www.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.dto.MvcScrBbsDto;
import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.entity.MvcRcmdtnInfo;
import com.moviecat.www.entity.MvcScrBbs;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.repository.MvcRcmdtnInfoRepository;
import com.moviecat.www.repository.MvcScrBbsRepository;
import com.moviecat.www.util.ColumnValueMapper;
import com.moviecat.www.util.PaginationUtil;
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
    private final ColumnValueMapper columnValueMapper;
    private final PaginationUtil paginationUtil;

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
    public String scrBbsRead(long scrId) throws JsonProcessingException {
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

    @Transactional
    public String scrList(long menuId, int page) throws JsonProcessingException {
        List<MvcScrBbs> scrListOrigin = mvcScrBbsRepository.findByMenuIdAndDeltYnOrderByScrIdDesc(menuId, "N");
        List<Map<String,Object>> scrList = new ArrayList<>();
        int scrNumber = scrListOrigin.size();
        for(MvcScrBbs scr : scrListOrigin){
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("scrNumber", scrNumber--);
            map.put("scrId",scr.getScrId());
            map.put("vdoNm",scr.getVdoNm());
            map.put("OpngDay",scr.getOpngDay());
            String[] rgstTime = timeFormat.formatDateToday(scr.getRgstDay());
            map.put("new",rgstTime[1]);
            map.put("rgstDate", rgstTime[0]);
            map.put("vdoEvl",scr.getVdoEvl());
            int rcmdTotal = columnValueMapper.pstIdAndMenuIdToRcmdTotal(scr.getScrId(), scr.getMenuId());
            map.put("rmcdTotal", (rcmdTotal > 5)? "5+" : String.valueOf(rcmdTotal));
            map.put("nickNm", columnValueMapper.mbrIdToNickNm(scr.getRgstUserId()));
            scrList.add(map);
        }
        List<Map<String, Object>> pagedPostList;
        try {
            pagedPostList = paginationUtil.getPage(scrList, page);
        } catch (Exception e) {
            throw e;
        }
        long total = scrList.size();
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("data", pagedPostList);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonScrList = objectMapper.writeValueAsString(result);

        return jsonScrList;
    }
}
