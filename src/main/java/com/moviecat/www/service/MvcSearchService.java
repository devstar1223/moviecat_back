package com.moviecat.www.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.entity.MvcScrBbs;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.repository.MvcScrBbsRepository;
import com.moviecat.www.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.moviecat.www.repository.MvcBbsRepository;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MvcSearchService {

    private final MvcBbsRepository mvcBbsRepository;
    private final MvcPageReturnService mvcPageReturnService;
    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    private final MvcScrBbsRepository mvcScrBbsRepository;

    @Transactional
    public String searchTtl(Long menuId, String srchWord, int page, int limit) { // 제목 검색으로, 요청 들어오면
        try {
            List<MvcBbs> resultList;
            if (menuId != 0) {
                resultList = mvcBbsRepository.findByMenuIdAndDeltYnAndTtlContainingOrderByRgstDayDesc(menuId, "N", srchWord); //찾고
            } else {
                resultList = mvcBbsRepository.findByDeltYnAndTtlContainingOrderByRgstDayDesc("N", srchWord); //찾고
            }
            if(resultList.isEmpty()) {
                throw new RuntimeException("검색 결과가 없습니다.");
            }
            Map<String, Object> pagedSearchResultMap = mvcPageReturnService.boardPageReturn(resultList, page, limit); //모든 행 넘겨주면, 페이징된 Map으로 받음
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(pagedSearchResultMap); // json으로 파싱해서 반환
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional
    public String searchTtlCn(Long menuId, String srchWord, int page, int limit) { // 제목+내용 검색으로, 요청 들어오면
        try {
            List<MvcBbs> resultList;
            if (menuId != 0) {
                resultList = mvcBbsRepository.findByMenuIdAndDeltYnAndTtlContainingOrMenuIdAndDeltYnAndCnContainingOrderByRgstDayDesc(menuId, "N", srchWord, menuId, "N", srchWord); //찾고
            } else {
                resultList = mvcBbsRepository.findByDeltYnAndTtlContainingOrDeltYnAndCnContainingOrderByRgstDayDesc("N", srchWord, "N", srchWord); //찾고
            }
            if(resultList.isEmpty()) {
                throw new RuntimeException("검색 결과가 없습니다.");
            }
            Map<String, Object> pagedSearchResultMap = mvcPageReturnService.boardPageReturn(resultList, page, limit); //모든 행 넘겨주면, 페이징된 Map으로 받음
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(pagedSearchResultMap); // json으로 파싱해서 반환
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional
    public String searchWriter(Long menuId, String srchWord, int page, int limit) { // 글쓴이 검색으로, 요청 들어오면
        Optional<MvcMbrInfo> mbrInfoOptional = mvcMbrInfoRepository.findByNickNm(srchWord); // 닉네임으로 ID 찾아오기
        if (mbrInfoOptional.isPresent()) {
            String writerId = mbrInfoOptional.get().getMbrId();
            List<MvcBbs> resultList;
            if (menuId != 0) {
                resultList = mvcBbsRepository.findByMenuIdAndDeltYnAndRgstUserIdOrderByRgstDayDesc(menuId, "N", writerId); //찾고
            } else {
                resultList = mvcBbsRepository.findByDeltYnAndRgstUserIdOrderByRgstDayDesc("N", writerId); //찾고
            }
            Map<String, Object> pagedSearchResultMap = mvcPageReturnService.boardPageReturn(resultList, page, limit); //모든 행 넘겨주면, 페이징된 Map으로 받음
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(pagedSearchResultMap); // json으로 파싱해서 반환
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
            }
        } else {
            throw new RuntimeException("검색결과가 없습니다.");
        }
    }

    public String searchScr(Long menuId, String srchWord, int page, int limit) {
        try {
            List<MvcScrBbs> scrList = mvcScrBbsRepository.findByVdoNmOrderByRgstDayDesc(srchWord); // 부분일치로 하려면 VdoNm 뒤에 Containing
            if(scrList.isEmpty()) {
                throw new RuntimeException("검색 결과가 없습니다.");
            }
            Map<String, Object> pagedSearchResultMap = mvcPageReturnService.scrPageReturn(scrList, page, limit); //모든 행 넘겨주면, 페이징된 Map으로 받음
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(pagedSearchResultMap); // json으로 파싱해서 반환
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        }

    }
}
