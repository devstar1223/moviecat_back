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

        List<MvcScrBbs> scrListOrigin = mvcScrBbsRepository.findByMenuIdAndDeltYnOrderByScrIdDesc(menuId, "N");
        List<Map<String,Object>> scrList = new ArrayList<>();

        for(MvcScrBbs scr : scrListOrigin) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("scrId", scr.getScrId());
            map.put("vdoNm", scr.getVdoNm());
            map.put("vdoNmEn", scr.getVdoNmEn());
            map.put("opngYear", scr.getOpngYear());
            map.put("scr", scr.getScr());
            map.put("rgstDate", timeFormat.formatDate(scr.getRgstDay()));
            map.put("vdoEvl", scr.getVdoEvl());
            map.put("nickNm", columnValueMapper.mbrIdToNickNm(scr.getRgstUserId()));

            int rcmdTotal = columnValueMapper.pstIdAndMenuIdToRcmdTotal(scr.getScrId(), scr.getMenuId());
            map.put("likeCnt", rcmdTotal);

            String likeYn = "N";
            if (!"".equals(mbrId) && mvcRcmdtnInfoService.rcmdCheck(menuId, scr.getScrId(), mbrId)) {
                likeYn = "Y";
            }
            map.put("likeYn", likeYn);
            scrList.add(map);
        }

        List<Map<String, Object>> pagedPostList = paginationUtil.getPageLimit(scrList, page, limit);

        Map<String, Object> result = new HashMap<>();
        result.put("total", scrList.size());
        result.put("data", pagedPostList);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(result);
    }
}
