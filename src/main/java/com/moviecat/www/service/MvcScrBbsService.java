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
import com.moviecat.www.util.ColumnValueMapper;
import com.moviecat.www.util.PaginationUtil;
import com.moviecat.www.util.TimeFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final MvcRcmdtnInfoService mvcRcmdtnInfoService;

    @Transactional
    public void scrBbsWrite(MvcScrBbsDto mvcScrBbsDto){
        MvcScrBbs newScr = new MvcScrBbs();
        newScr.setMenuId(mvcScrBbsDto.getMenuId());
        newScr.setVdoCode(mvcScrBbsDto.getVdoCode());
        newScr.setVdoNm(mvcScrBbsDto.getVdoNm());
        newScr.setVdoNmEn(mvcScrBbsDto.getVdoNmEn());
        newScr.setOpngYear(mvcScrBbsDto.getOpngYear());
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
    public void scrBbsDelete(MvcScrBbsDto mvcScrBbsDto) {
        Optional<MvcScrBbs> scrOptional = mvcScrBbsRepository.findById(mvcScrBbsDto.getScrId());
        if(scrOptional.isPresent()){
            MvcScrBbs scr = scrOptional.get();
            scr.setDeltYn("Y");
            scr.setMdfcnUserId(mvcScrBbsDto.getMbrId());
            scr.setMdfcnUserNm(mvcScrBbsDto.getMbrNm());
            scr.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
            mvcScrBbsRepository.save(scr);
        } else {
            throw new NoSuchElementException("해당 평점이 존재하지 않습니다.");
        }
    }

    @Transactional
    public String scrList(long menuId, String mbrId, int page, int limit) throws Exception {

        // 페이징 처리해서 가져옴
        PageRequest pageRequest = PageRequest.of(page-1, limit);
        Page<Object[]> resultPage = mvcScrBbsRepository.findWithCountsAndLikeYnByUserId(mbrId,menuId, pageRequest);

        // 페이징된 데이터를 pagedPostList로 변환
        List<Object[]> pagedScrList = resultPage.getContent();

        // 포맷팅된 List를 담을 새로운 List 생성
        List<Map<String, Object>> scrList = new ArrayList<>();

        for(Object[] scr : pagedScrList) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("scrId", scr[0]);
            map.put("vdoNm", scr[1]);
            map.put("vdoNmEn", scr[2]);
            map.put("opngYear", scr[3]);
            map.put("scr", scr[4]);
            map.put("rgstDate", timeFormat.formatDate((Timestamp)scr[5]));
            map.put("vdoEvl", scr[6]);
            map.put("nickNm", scr[7]);
            map.put("likeCnt", scr[8]);
            map.put("likeYn", scr[9]);
            scrList.add(map);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", scrList.size());
        result.put("data", scrList);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(result);
    }
}
