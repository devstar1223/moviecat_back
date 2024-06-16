package com.moviecat.www.service;

import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MvcPageReturnService {
    public Map<String, Object> boardPageReturn(List<MvcBbs> resultList, int page) {
        if (resultList == null || resultList.isEmpty()) {
            throw new RuntimeException("검색결과가 없습니다.");
        }

        int totalSize = resultList.size();
        if (totalSize == 0) {
            throw new RuntimeException("검색결과가 없습니다.");
        }

        List<Map<String, Object>> searchResultList = new ArrayList<>(); // 글 여러개 담을 리스트
        for (MvcBbs searchResult : resultList) { // 받아온 글 목록 하나씩 for 문으로 처리하기
            Map<String, Object> searchResultMap = new LinkedHashMap<>(); // LinkedHashMap을 사용하여 순서를 보장
            searchResultMap.put("menuId", searchResult.getMenuId());
            searchResultMap.put("pstId", searchResult.getPstId());
            searchResultMap.put("ttl", searchResult.getTtl());
            searchResultMap.put("spoYn", searchResult.getSpoYn());

            //Optional<MvcMbrInfo> mbrInfoOptional = mvcMbrInfoRepository.findByRgstUserId(searchResult.getRgstUserId()); // 등록id로 유저 찾아오기
            //MvcMbrInfo mbrInfo = mbrInfoOptional.get();
            //searchResultMap.put("nickNm", mbrInfo.getNickNm()); // nickNm 찾아 넣기
            searchResultMap.put("nickNm", "가데이터 오류 임시 값"); // DB테이블 정리하고 테스트 하면됨

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd/HH:mm");
            String rgstTime = sdf.format(searchResult.getRgstDay());
            searchResultMap.put("rgstDay", rgstTime);

            searchResultList.add(searchResultMap);
        }

        int pageSize = PaginationUtil.PAGE_SIZE;
        int totalPages = (int) Math.ceil((double) totalSize / pageSize);

        if (page > totalPages) {
            throw new RuntimeException("페이지를 초과합니다.");
        }

        List<Map<String, Object>> pagedResultList = PaginationUtil.getPage(searchResultList, page); // 여러 글 담은 리스트 페이징 해주기

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("total", totalSize);
        responseMap.put("data", pagedResultList);

        return responseMap;
    }
}
