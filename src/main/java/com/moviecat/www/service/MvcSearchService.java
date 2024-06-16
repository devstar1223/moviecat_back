package com.moviecat.www.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.moviecat.www.repository.MvcBbsRepository;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MvcSearchService {

    private final MvcBbsRepository mvcBbsRepository;
    private final MvcPageReturnService mvcPageReturnService;
    private final MvcMbrInfoRepository mvcMbrInfoRepository;

    public String searchTtl(Long menuId, String srchWord, int page) { // 제목 검색으로, 요청 들어오면
        try {
            List<MvcBbs> resultList = new ArrayList<>();
            if(menuId != 0) {
                resultList = mvcBbsRepository.findByMenuIdAndDeltYnAndTtlContainingOrderByRgstDayDesc(menuId, "N", srchWord); //찾고
            }
            else{
                resultList = mvcBbsRepository.findByDeltYnAndTtlContainingOrderByRgstDayDesc("N", srchWord); //찾고
            }
            Map<String,Object> pagedSearchResultMap = mvcPageReturnService.boardPageReturn(resultList,page); //모든 행 넘겨주면, 페이징된 Map으로 받음
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(pagedSearchResultMap); // json으로 파싱해서 반환
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String searchTtlCn(Long menuId, String srchWord, int page) { // 제목+내용 검색으로, 요청 들어오면
        try {
            List<MvcBbs> resultList = new ArrayList<>();
            if(menuId != 0) {
                resultList = mvcBbsRepository.findByMenuIdAndDeltYnAndTtlContainingOrMenuIdAndDeltYnAndCnContainingOrderByRgstDayDesc(menuId, "N", srchWord,menuId, "N", srchWord); //찾고
            }
            else{
                resultList = mvcBbsRepository.findByDeltYnAndTtlContainingOrDeltYnAndCnContainingOrderByRgstDayDesc("N", srchWord,"N", srchWord); //찾고
            }
            Map<String,Object> pagedSearchResultMap = mvcPageReturnService.boardPageReturn(resultList,page); //모든 행 넘겨주면, 페이징된 Map으로 받음
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(pagedSearchResultMap); // json으로 파싱해서 반환
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //TODO.작성자검색 테스트 미완료(아마 될것)
    public String searchWriter(Long menuId, String srchWord, int page) { // 글쓴이 검색으로, 요청 들어오면
        try {
            Optional<MvcMbrInfo> mbrInfoOptional = mvcMbrInfoRepository.findByNickNm(srchWord); // 닉네임으로 ID 찾아오기
            String writerId = mbrInfoOptional.get().getMbrId();
            List<MvcBbs> resultList = new ArrayList<>();
            if(menuId != 0) {
                resultList = mvcBbsRepository.findByMenuIdAndDeltYnAndRgstUserIdOrderByRgstDayDesc(menuId, "N", writerId); //찾고
            }
            else{
                resultList = mvcBbsRepository.findByDeltYnAndRgstUserIdOrderByRgstDayDesc("N", writerId); //찾고
            }
            Map<String,Object> pagedSearchResultMap = mvcPageReturnService.boardPageReturn(resultList,page); //모든 행 넘겨주면, 페이징된 Map으로 받음
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(pagedSearchResultMap); // json으로 파싱해서 반환
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
