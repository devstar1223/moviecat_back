package com.moviecat.www.service;

import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.util.PaginationUtil;
import com.moviecat.www.util.TimeFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.moviecat.www.entity.MvcMbrInfo;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MvcPageReturnService {

    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    private final TimeFormat timeFormat;
    private final PaginationUtil paginationUtil;
    public Map<String, Object> boardPageReturn(List<MvcBbs> resultList, int page) {
        if (resultList == null || resultList.isEmpty()) {
            throw new NoSuchElementException("검색결과가 없습니다.");
        }

        int totalSize = resultList.size();
        if (totalSize == 0) {
            throw new NoSuchElementException("검색결과가 없습니다.");
        }

        List<Map<String, Object>> searchResultList = new ArrayList<>(); // 글 여러개 담을 리스트
        for (MvcBbs searchResult : resultList) { // 받아온 글 목록 하나씩 for 문으로 처리하기
            Map<String, Object> searchResultMap = new LinkedHashMap<>(); // LinkedHashMap을 사용하여 순서를 보장
            searchResultMap.put("menuId", searchResult.getMenuId());
            searchResultMap.put("pstId", searchResult.getPstId());
            searchResultMap.put("ttl", searchResult.getTtl());
            searchResultMap.put("spoYn", searchResult.getSpoYn());

            Optional<MvcMbrInfo> mbrInfoOptional = mvcMbrInfoRepository.findByRgstUserId(searchResult.getRgstUserId()); // 등록id로 유저 찾아오기
            MvcMbrInfo mbrInfo = mbrInfoOptional.get();
            searchResultMap.put("nickNm", mbrInfo.getNickNm()); // nickNm 찾아 넣기
            String rgstTime = timeFormat.formatDate(mbrInfo.getRgstDay());
            searchResultMap.put("rgstDay", rgstTime);

            searchResultList.add(searchResultMap);
        }

        int pageSize = PaginationUtil.PAGE_SIZE;
        int totalPages = (int) Math.ceil((double) totalSize / pageSize);

        if (page > totalPages) {
            throw new RuntimeException("페이지를 초과합니다.");
        }

        List<Map<String, Object>> pagedResultList = paginationUtil.getPage(searchResultList, page); // 여러 글 담은 리스트 페이징 해주기

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("total", totalSize);
        responseMap.put("data", pagedResultList);

        return responseMap;
    }
}
