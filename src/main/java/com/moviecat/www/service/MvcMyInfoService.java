package com.moviecat.www.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.dto.MvcMbrInfoDto;
import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.entity.MvcBbsCmnt;
import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.entity.MvcScrBbs;
import com.moviecat.www.repository.MvcBbsCmntRepository;
import com.moviecat.www.repository.MvcBbsRepository;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.repository.MvcScrBbsRepository;
import com.moviecat.www.util.ColumnValueMapper;
import com.moviecat.www.util.PaginationUtil;
import com.moviecat.www.util.TimeFormat;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MvcMyInfoService {

    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final MvcBbsRepository mvcBbsRepository;
    private final MvcScrBbsRepository mvcScrBbsRepository;
    private final MvcBbsCmntRepository mvcBbsCmntRepository;
    private final ColumnValueMapper columnValueMapper;
    private final TimeFormat timeFormat;
    private final PaginationUtil paginationUtil;
    private final MvcFileUploadService mvcFileUploadService;

    public String myPostRead(String mbrId, int page, int limit) throws JsonProcessingException {

        List<MvcBbs> postListOrigin = mvcBbsRepository.findByRgstUserIdAndDeltYnOrderByRgstDayDesc(mbrId, "N");
        if(!postListOrigin.isEmpty()){
            List<Map<String,Object>> myPostList = new ArrayList<>();
            for(MvcBbs myPost : postListOrigin){
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("menuId",myPost.getMenuId());
                map.put("pstId",myPost.getPstId());
                map.put("spoYn",myPost.getSpoYn());
                map.put("ttl",myPost.getTtl());
                map.put("cmntTotal",columnValueMapper.pstIdToCmntTotal(myPost.getPstId()));
                map.put("RcmdTotal",columnValueMapper.pstIdAndMenuIdToRcmdTotal(myPost.getPstId(),myPost.getMenuId()));
                String[] rgstTime = timeFormat.formatDateToday(myPost.getRgstDay());
//                map.put("new", rgstTime[1]);
                map.put("rgstDate", rgstTime[0]);
                myPostList.add(map);
            }
            List<Map<String, Object>> pagedMyPostList;
            try {
                pagedMyPostList = paginationUtil.getPageLimit(myPostList, page, limit);
            } catch (Exception e) {
                throw e;
            }
            long total = myPostList.size();
            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("data", pagedMyPostList);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMyPostList = objectMapper.writeValueAsString(result);

            return jsonMyPostList;
        }
        else{
            throw new NoSuchElementException("작성한 글이 없습니다.");
        }
    }

    public String myCmntRead(String mbrId, int page, int limit) throws JsonProcessingException {
        List<MvcBbsCmnt> cmntListOrigin = mvcBbsCmntRepository.findByRgstUserIdAndDeltYnOrderByRgstDayDesc(mbrId, 'N');
        if(!cmntListOrigin.isEmpty()){
            List<Map<String,Object>> myCmntList = new ArrayList<>();
            for(MvcBbsCmnt myCmnt : cmntListOrigin){
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("menuId",columnValueMapper.pstIdToMenuId(myCmnt.getPstId()));
                map.put("cmntId",myCmnt.getCmntId());
                map.put("cn",myCmnt.getCn());
                String[] rgstTime = timeFormat.formatDateToday(myCmnt.getRgstDay());
//                map.put("new", rgstTime[1]);
                map.put("rgstDate", rgstTime[0]);
                myCmntList.add(map);
            }
            List<Map<String, Object>> pagedMyCmntList;
            try {
                pagedMyCmntList = paginationUtil.getPageLimit(myCmntList, page, limit);
            } catch (Exception e) {
                throw e;
            }
            long total = myCmntList.size();
            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("data", pagedMyCmntList);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMyCmntList = objectMapper.writeValueAsString(result);

            return jsonMyCmntList;
        }
        else{
            throw new NoSuchElementException("작성한 댓글이 없습니다.");
        }
    }

    public String myScrRead(String mbrId, int page, int limit) throws JsonProcessingException {
        List<MvcScrBbs> scrListOrigin = mvcScrBbsRepository.findByRgstUserIdAndDeltYnOrderByRgstDayDesc(mbrId, "N");
        if(!scrListOrigin.isEmpty()){
            List<Map<String,Object>> myScrList = new ArrayList<>();
            for(MvcScrBbs myScr : scrListOrigin){
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("scrId",myScr.getScrId());
                map.put("vdoNm",myScr.getVdoNm());
                map.put("scr",myScr.getScr());
                map.put("vdoEvl",myScr.getVdoEvl());
                String[] rgstTime = timeFormat.formatDateToday(myScr.getRgstDay());
//                map.put("new", rgstTime[1]);
                map.put("rgstDate", rgstTime[0]);
                myScrList.add(map);
            }
            List<Map<String, Object>> pagedMyScrList;
            try {
                pagedMyScrList = paginationUtil.getPageLimit(myScrList, page, limit);
            } catch (Exception e) {
                throw e;
            }
            long total = myScrList.size();
            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("data", pagedMyScrList);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMyScrList = objectMapper.writeValueAsString(result);

            return jsonMyScrList;
        }
        else{
            throw new NoSuchElementException("작성한 평점이 없습니다.");
        }
    }
}
