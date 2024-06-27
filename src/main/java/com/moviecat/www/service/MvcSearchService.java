package com.moviecat.www.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.entity.MvcScrBbs;
import com.moviecat.www.repository.MvcBbsRepository;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.repository.MvcScrBbsRepository;
import com.moviecat.www.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MvcSearchService {

    private final MvcBbsRepository mvcBbsRepository;
    private final MvcPageReturnService mvcPageReturnService;
    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    private final MvcScrBbsRepository mvcScrBbsRepository;

    @Transactional
    public String searchScr(String srchWord, int page, int limit,String mbrId) {
        try {
            List<MvcScrBbs> scrList = mvcScrBbsRepository.findByDeltYnAndVdoNmContainingOrVdoNmEnContainingOrderByRgstDayDesc("N", srchWord, srchWord); // 한글이름 부분일치 or 영문이름 부분일치
            if (scrList.isEmpty()) {
                // 검색 결과가 없을 때 처리000
                return "{\"total\": 0, \"data\": []}";
            }
            Map<String, Object> pagedSearchResultMap = mvcPageReturnService.scrPageReturn(scrList, page, limit, mbrId); // 페이징된 결과 가져오기
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(pagedSearchResultMap); // JSON 형태로 반환
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional(readOnly = true)
    public String search(Long menuId, int div, String srchWord, int page, int limit) {
        try {
            List<MvcBbs> resultList = null;

            if (div == 1) {
                resultList = mvcBbsRepository.findByMenuIdAndDeltYnAndTtlContainingOrderByRgstDayDesc(menuId, "N", srchWord); // 제목 검색
            } else if (div == 2) {
                resultList = mvcBbsRepository.findByMenuIdAndDeltYnAndTtlContainingOrMenuIdAndDeltYnAndCnContainingOrderByRgstDayDesc(menuId, "N", srchWord, menuId, "N", srchWord); // 제목 + 내용 검색
            } else if (div == 3) {
                Optional<MvcMbrInfo> mbrInfoOptional = mvcMbrInfoRepository.findByNickNm(srchWord);
                String writerId = mbrInfoOptional.isPresent() ? mbrInfoOptional.get().getMbrId() : null;
                if (writerId != null) {
                    resultList = mvcBbsRepository.findByMenuIdAndDeltYnAndRgstUserIdOrderByRgstDayDesc(menuId, "N", writerId); // 작성자 닉네임 검색
                }
            }

            if (resultList == null || resultList.isEmpty()) {
                // 검색 결과가 없을 때 처리
                return "{\"total\": 0, \"data\": []}";
            }

            Map<String, Object> pagedSearchResultMap = mvcPageReturnService.boardPageReturn(resultList, page, limit); // 페이징된 결과 가져오기

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(pagedSearchResultMap); // JSON 형태로 반환

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        }
    }


    @Transactional(readOnly = true)
    public String searchTotal(String srchWord, int page, int limit) {
        try {
            List<MvcBbs> resultList = mvcBbsRepository.findByDeltYnAndTtlContainingOrderByRgstDayDesc("N", srchWord); // 제목 전체 검색

            if (resultList == null || resultList.isEmpty()) {
                // 검색 결과가 없을 때 처리
                return "{\"total\": 0, \"data\": []}";
            }

            Map<String, Object> pagedSearchResultMap = mvcPageReturnService.totalSearchReturn(resultList, page, limit); // 페이징된 결과 가져오기

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(pagedSearchResultMap); // JSON 형태로 반환

        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        }
    }
}
