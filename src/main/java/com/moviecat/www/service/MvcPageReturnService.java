package com.moviecat.www.service;

import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.entity.MvcScrBbs;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.util.ColumnValueMapper;
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
    private final ColumnValueMapper columnValueMapper;
    public Map<String, Object> boardPageReturn(List<MvcBbs> resultList, int page) {
        if (resultList == null || resultList.isEmpty()) {
            throw new NoSuchElementException("검색결과가 없습니다.");
        }

        int totalSize = resultList.size();
        if (totalSize == 0) {
            throw new NoSuchElementException("검색결과가 없습니다.");
        }
        int postNumber = totalSize;
        List<Map<String, Object>> searchResultList = new ArrayList<>(); // 글 여러개 담을 리스트
        for (MvcBbs searchResult : resultList) { // 받아온 글 목록 하나씩 for 문으로 처리하기
            Map<String, Object> searchResultMap = new LinkedHashMap<>(); // LinkedHashMap을 사용하여 순서를 보장
            searchResultMap.put("menuId", searchResult.getMenuId());
            searchResultMap.put("postNumber", postNumber--);
            searchResultMap.put("pstId", searchResult.getPstId());
            String[] rgstTime = timeFormat.formatDateToday(searchResult.getRgstDay());
            searchResultMap.put("new",rgstTime[1]);
            searchResultMap.put("rgstDate", rgstTime[0]);
            searchResultMap.put("spoYn", searchResult.getSpoYn());
            searchResultMap.put("ttl", searchResult.getTtl());
            searchResultMap.put("cmntTotal", columnValueMapper.pstIdToCmntTotal(searchResult.getPstId()));
            int rcmdTotal = columnValueMapper.pstIdAndMenuIdToRcmdTotal(searchResult.getPstId(), searchResult.getMenuId());
            searchResultMap.put("rmcdTotal", (rcmdTotal > 5)? "5+" : String.valueOf(rcmdTotal));
            searchResultMap.put("nickNm", columnValueMapper.mbrIdToNickNm(searchResult.getRgstUserId()));

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

    public Map<String, Object> scrPageReturn(List<MvcScrBbs> resultList, int page) {
        if (resultList == null || resultList.isEmpty()) {
            throw new NoSuchElementException("검색결과가 없습니다.");
        }

        int totalSize = resultList.size();
        if (totalSize == 0) {
            throw new NoSuchElementException("검색결과가 없습니다.");
        }

        List<Map<String, Object>> searchResultList = new ArrayList<>(); // 글 여러개 담을 리스트
        int scrNumber = totalSize;
        for (MvcScrBbs searchResult : resultList) { // 받아온 글 목록 하나씩 for 문으로 처리하기
            Map<String, Object> searchResultMap = new LinkedHashMap<>(); // LinkedHashMap을 사용하여 순서를 보장
            searchResultMap.put("scrNumber", scrNumber--);
            searchResultMap.put("scrId", searchResult.getScrId());
            searchResultMap.put("vdoNm", searchResult.getVdoEvl());
            searchResultMap.put("OpngDay", searchResult.getOpngYear());
            String[] rgstTime = timeFormat.formatDateToday(searchResult.getRgstDay());
            searchResultMap.put("new",rgstTime[1]);
            searchResultMap.put("rgstDate", rgstTime[0]);
            searchResultMap.put("vdoEvl", searchResult.getVdoEvl());

            int rcmdTotal = columnValueMapper.pstIdAndMenuIdToRcmdTotal(searchResult.getScrId(), searchResult.getMenuId());
            searchResultMap.put("rmcdTotal", (rcmdTotal > 5)? "5+" : String.valueOf(rcmdTotal));
            searchResultMap.put("nickNm", columnValueMapper.mbrIdToNickNm(searchResult.getRgstUserId()));

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
