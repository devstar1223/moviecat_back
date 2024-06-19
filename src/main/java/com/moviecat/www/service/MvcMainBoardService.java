package com.moviecat.www.service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.entity.MvcScrBbs;
import com.moviecat.www.repository.MvcBbsRepository;
import com.moviecat.www.repository.MvcScrBbsRepository;
import com.moviecat.www.util.ColumnValueMapper;
import com.moviecat.www.util.TimeFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MvcMainBoardService {

    private final MvcBbsRepository mvcBbsRepository;
    private final MvcScrBbsRepository mvcScrBbsRepository;
    private final ColumnValueMapper columnValueMapper;
    private final TimeFormat timeFormat;

    public String mainBoard() throws JacksonException {
        Map<String, List<Map<String, Object>>> mainListMap = new LinkedHashMap<>();

        mainListMap.put("영화 리뷰", getBbsData(1L, 10));
        mainListMap.put("영화 토크", getBbsData(2L, 10));
        mainListMap.put("영화 평점", getScrData(8));

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(mainListMap);
    }

    private List<Map<String, Object>> getBbsData(Long menuId, int limit) {
        List<MvcBbs> bbsListOrigin = mvcBbsRepository.findByMenuIdAndDeltYnOrderByRgstDayDesc(menuId, "N");
        List<Map<String, Object>> bbsList = new ArrayList<>();
        int number = limit;

        for (MvcBbs bbs : bbsListOrigin) {
            if (number == 0) {
                break;
            }
            bbsList.add(createBbsMap(bbs, number--));
        }
        return bbsList;
    }

    private List<Map<String, Object>> getScrData(int limit) {
        List<MvcScrBbs> scrListOrigin = mvcScrBbsRepository.findByDeltYnOrderByScrIdDesc("N");
        List<Map<String, Object>> scrList = new ArrayList<>();
        int number = limit;

        for (MvcScrBbs scr : scrListOrigin) {
            if (number == 0) {
                break;
            }
            scrList.add(createScrMap(scr, number--));
        }
        return scrList;
    }

    private Map<String, Object> createBbsMap(MvcBbs bbs, int number) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("scrNumber", number);
        map.put("scrId", bbs.getPstId());
        map.put("ttl", bbs.getTtl());
        map.put("cmntTotal", columnValueMapper.pstIdToCmntTotal(bbs.getPstId()));
        map.put("rcmdTotal", columnValueMapper.pstIdAndMenuIdToRcmdTotal(bbs.getPstId(), bbs.getMenuId()));
        String[] rgstTime = timeFormat.formatDateToday(bbs.getRgstDay());
        map.put("new", rgstTime[1]);
        map.put("rgstDate", rgstTime[0]);
        return map;
    }

    private Map<String, Object> createScrMap(MvcScrBbs scr, int number) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("scrNumber", number);
        map.put("scrId", scr.getScrId());
        map.put("vdoNm", scr.getVdoNm());
        map.put("vdoEvl", scr.getVdoEvl());
        map.put("scr", scr.getScr());
        map.put("nickNm", columnValueMapper.mbrIdToNickNm(scr.getRgstUserId()));
        map.put("rcmdTotal", columnValueMapper.pstIdAndMenuIdToRcmdTotal(scr.getScrId(), scr.getMenuId()));
        String[] rgstTime = timeFormat.formatDateToday(scr.getRgstDay());
        map.put("new", rgstTime[1]);
        map.put("rgstDate", rgstTime[0]);
        return map;
    }
}