package com.moviecat.www.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.entity.MvcBbs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.moviecat.www.repository.MvcBbsRepository;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MvcSearchService {

    private final MvcBbsRepository mvcBbsRepository;

    public String searchAll(Long menuId, int srchCrtr, String srchWord) {
        try {
            if (srchCrtr == 0) {
                List<MvcBbs> resultList = mvcBbsRepository.findByMenuIdAndDeltYnAndTtlContainingOrderByRgstDayDesc(menuId, "N", srchWord);
                List<Map<String, Object>> searchResultList = new ArrayList<>();
                for (MvcBbs searchResult : resultList) {
                    Map<String, Object> searchResultMap = new LinkedHashMap<>(); // LinkedHashMap을 사용하여 순서를 보장
                    searchResultMap.put("menuId", searchResult.getMenuId());
                    searchResultMap.put("pstId", searchResult.getPstId());
                    searchResultMap.put("ttl", searchResult.getTtl());
                    //Optional<MvcMbrInfo> mbrInfoOptional = mvcMbrInfoRepository.findByRgstUserId(searchResult.getRgstUserId()); // 등록id로 유저 찾아오기
                    //MvcMbrInfo mbrInfo = mbrInfoOptional.get();
                    //searchResultMap.put("nickNm", mbrInfo.getNickNm()); // nickNm 찾아 넣기
                    searchResultMap.put("nickNm", "가데이터 오류 임시 값"); // 예외 처리를 했을 때의 대안 값을 넣어줍니다.

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd/HH:mm");
                    String rgstTime = sdf.format(searchResult.getRgstDay());
                    searchResultMap.put("rgstDay", rgstTime);

                    searchResultList.add(searchResultMap);
                }

                Map<String, Object> responseMap = new LinkedHashMap<>(); // LinkedHashMap을 사용하여 순서를 보장
                responseMap.put("totalCount", searchResultList.size());
                responseMap.put("data", searchResultList);

                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(responseMap);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
