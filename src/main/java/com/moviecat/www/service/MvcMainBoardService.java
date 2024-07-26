package com.moviecat.www.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.entity.MvcScrBbs;
import com.moviecat.www.entity.MvcMenu;
import com.moviecat.www.repository.MvcBbsRepository;
import com.moviecat.www.repository.MvcScrBbsRepository;
import com.moviecat.www.repository.MvcMenuRepository;
import com.moviecat.www.util.ColumnValueMapper;
import com.moviecat.www.util.TimeFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.moviecat.www.config.StaticConstantConfig;


@Service
@RequiredArgsConstructor
public class MvcMainBoardService {

    private final MvcBbsRepository mvcBbsRepository;
    private final MvcScrBbsRepository mvcScrBbsRepository;
    private final MvcMenuRepository mvcMenuRepository;
    private final ColumnValueMapper columnValueMapper;
    private final TimeFormat timeFormat;

    public String mainBoard(long menuId1, long menuId2, long menuId3) {
        Map<String, Object> mainListMap = new LinkedHashMap<>();

        int hotPostCount = StaticConstantConfig.MAIN_HOT_POST_COUNT;
        int generalPostCount = StaticConstantConfig.MAIN_GENERAL_POST_COUNT;
        int movieScoreCount = StaticConstantConfig.MAIN_MOVIE_SCORE_COUNT;

        addMenuInfo(mainListMap, "menu1", menuId1, generalPostCount, hotPostCount);
        addMenuInfo(mainListMap, "menu2", menuId2, generalPostCount, hotPostCount);
        addMovieScore(mainListMap, movieScoreCount, menuId3);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(mainListMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        }
    }

    private void addMenuInfo(Map<String, Object> mainListMap, String category, Long menuId, int generalLimit, int hotLimit) {
        List<Map<String, Object>> hotList = getBbsData(menuId, hotLimit);
        List<Map<String, Object>> generalList = getBbsData(menuId, generalLimit);

        Map<String, Object> categoryMap = new LinkedHashMap<>();
        categoryMap.put("menuInfo", createMenuInfo(menuId));
        categoryMap.put("hot", hotList);
        categoryMap.put("general", generalList);

        mainListMap.put(category, categoryMap);
    }

    private void addMovieScore(Map<String, Object> mainListMap, int limit, long menuId3) {
        List<MvcScrBbs> scrListOrigin = mvcScrBbsRepository.findByDeltYnOrderByScrIdDesc("N");
        List<Map<String, Object>> scrList = new ArrayList<>();
        int number = limit;

        for (MvcScrBbs scr : scrListOrigin) {
            if (number == 0) {
                break;
            }
            scrList.add(createScrMap(scr, number--));
        }

        Map<String, Object> movieScoreMap = new LinkedHashMap<>();
        movieScoreMap.put("menuInfo", createMenuInfo(menuId3));
        movieScoreMap.put("general", scrList);

        mainListMap.put("menu3", movieScoreMap);
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
        map.put("scrId", bbs.getPstId());
        map.put("ttl", bbs.getTtl());
        map.put("cmntTotal", columnValueMapper.pstIdToCmntTotal(bbs.getPstId()));
        map.put("rcmdTotal", columnValueMapper.pstIdAndMenuIdToRcmdTotal(bbs.getPstId(), bbs.getMenuId()));
        map.put("nickNm", columnValueMapper.mbrIdToNickNm(bbs.getRgstUserId()));
        String[] rgstTime = timeFormat.formatDateToday(bbs.getRgstDay());
        map.put("new", rgstTime[1]);
        map.put("rgstDate", rgstTime[0]);
        return map;
    }

    private Map<String, Object> createScrMap(MvcScrBbs scr, int number) {
        Map<String, Object> map = new LinkedHashMap<>();
//        map.put("scrNumber", number);
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

    private Map<String, Object> createMenuInfo(Long menuId) {
        Optional<MvcMenu> menuOptional = mvcMenuRepository.findById(menuId);
        if (menuOptional.isPresent()) {
            MvcMenu menu = menuOptional.get();
            Map<String, Object> menuInfo = new LinkedHashMap<>();
            menuInfo.put("menuId", menu.getMenuId());
            menuInfo.put("menuNm", menu.getMenuNm());
            return menuInfo;
        } else {
            throw new IllegalArgumentException("Menu not found for menuId: " + menuId);
        }
    }
}
