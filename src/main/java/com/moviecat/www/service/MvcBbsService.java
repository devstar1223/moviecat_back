package com.moviecat.www.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.dto.MvcBbsDto;
import com.moviecat.www.entity.MvcAtchFile;
import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.entity.MvcRcmdtnInfo;
import com.moviecat.www.repository.MvcAtchFileRepository;
import com.moviecat.www.repository.MvcBbsRepository;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.repository.MvcRcmdtnInfoRepository;
import com.moviecat.www.util.ColumnValueMapper;
import com.moviecat.www.util.FileUtils;
import com.moviecat.www.util.PaginationUtil;
import com.moviecat.www.util.TimeFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MvcBbsService {
    private final MvcBbsRepository mvcBbsRepository;
    private final MvcRcmdtnInfoRepository mvcRcmdtnInfoRepository;
    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    private final MvcAtchFileRepository mvcAtchFileRepository;
    private final TimeFormat timeFormat;
    private final PaginationUtil paginationUtil;
    private final FileUtils fileUtils;
    private final ColumnValueMapper columnValueMapper;

    @Transactional
    public void bbsWritePost(MvcBbsDto mvcBbsDto) {
        MvcBbs newPost = new MvcBbs();
        newPost.setMenuId(mvcBbsDto.getMenuId());
        newPost.setTtl(mvcBbsDto.getTtl());
        newPost.setCn(mvcBbsDto.getCn());
        newPost.setAtchFileid(mvcBbsDto.getAtchFileId());
        newPost.setSpoYn(mvcBbsDto.getSpoYn());
        newPost.setRgstUserId(mvcBbsDto.getMbrId());
        newPost.setRgstUserNm(mvcBbsDto.getMbrNm());
        newPost.setRgstDay(Timestamp.valueOf(LocalDateTime.now())); // 현재시간
        newPost.setMdfcnUserId(mvcBbsDto.getMbrId());
        newPost.setMdfcnUserNm(mvcBbsDto.getMbrNm());
        newPost.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 현재시간 (수정 api 따로)
        newPost.setDeltYn("N"); // 글 쓰기 이므로, 기본적으로 "N"으로 설정
        mvcBbsRepository.save(newPost);
    }

    @Transactional
    public void bbsEditPost(MvcBbsDto newPostDto) {
        Optional<MvcBbs> bbsOptional = mvcBbsRepository.findById(newPostDto.getPstId()); // 받아온 정보로 글 찾기
        MvcBbs post = bbsOptional.get();
        post.setTtl(newPostDto.getTtl());
        post.setCn(newPostDto.getCn());
        post.setSpoYn(newPostDto.getSpoYn());
        post.setAtchFileid(newPostDto.getAtchFileId());
        post.setMdfcnUserId(newPostDto.getMbrId()); // 들어온 데이터의 id로 수정 id 등록
        post.setMdfcnUserNm(newPostDto.getMbrNm()); // 들어온 데이터의 이름으로 수정자 등록
        post.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 현재 시간
        mvcBbsRepository.save(post);
    }

    @Transactional
    public void bbsDeletePost(MvcBbsDto newPostDto) {
        Optional<MvcBbs> bbsOptional = mvcBbsRepository.findById(newPostDto.getPstId()); // 받아온 정보로 글 찾기
        MvcBbs post = bbsOptional.get();
        post.setMdfcnUserId(newPostDto.getMbrId()); // 들어온 데이터의 id로 수정 id 등록
        post.setMdfcnUserNm(newPostDto.getMbrNm()); // 들어온 데이터의 이름으로 수정자 등록
        post.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 현재 시간
        post.setDeltYn("Y"); // 글 삭제 이므로, "Y"로 설정
        mvcBbsRepository.save(post);
    }

    @Transactional
    public String bbsReadBoard(long menuId, int page) throws JsonProcessingException {
        List<MvcBbs> postListOrigin = mvcBbsRepository.findByMenuIdAndDeltYnOrderByPstIdDesc(menuId, "N");
        List<Map<String,Object>> postList = new ArrayList<>();
        int postNumber = postListOrigin.size();
        for(MvcBbs post : postListOrigin){
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("postNumber", postNumber--);
            map.put("pstId",post.getPstId());
            String[] rgstTime = timeFormat.formatDateToday(post.getRgstDay());
            map.put("new",rgstTime[1]);
            map.put("rgstDate", rgstTime[0]);
            map.put("spoYn",post.getSpoYn());
            map.put("ttl",post.getTtl());
            map.put("cmntTotal", columnValueMapper.pstIdToCmntTotal(post.getPstId()));
            int rcmdTotal = columnValueMapper.pstIdAndMenuIdToRcmdTotal(post.getPstId(), post.getMenuId());
            map.put("rmcdTotal", (rcmdTotal > 5)? "5+" : String.valueOf(rcmdTotal));
            map.put("nickNm", columnValueMapper.mbrIdToNickNm(post.getRgstUserId()));
            postList.add(map);
        }
        List<Map<String, Object>> pagedPostList;
        try {
            pagedPostList = paginationUtil.getPage(postList, page);
        } catch (Exception e) {
            throw e;
        }
        long total = postList.size();
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("data", pagedPostList);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPostList = objectMapper.writeValueAsString(result);

        return jsonPostList;
    }

    public String bbsReadPost(long menuId,long pstId, String mbrId) throws JsonProcessingException {
        Optional<MvcBbs> postOptional = mvcBbsRepository.findByMenuIdAndPstIdAndDeltYn(menuId,pstId,"N");

        if(postOptional.isPresent()){
            MvcBbs post = postOptional.get();
            Map<String, Object> postMap = new LinkedHashMap<>();
            postMap.put("ttl", post.getTtl());
            postMap.put("cn", post.getCn());
            postMap.put("spoYn", post.getSpoYn());
            String rgstTime = timeFormat.formatDate(post.getRgstDay());
            postMap.put("rgstDate", rgstTime);
            postMap.put("rcmd", columnValueMapper.pstIdAndMenuIdToRcmdTotal(post.getPstId(), post.getMenuId())); // 좋아요 수 구해오기
            postMap.put("rcmdDeltYn", columnValueMapper.mbrIdToRcmdDeltYn(mbrId)); // 글 조회시, 해당 유저가 이 글을 좋아요 눌렀는지.
            postMap.put("profileUrl", columnValueMapper.mbrIdToAtchFileUrl(post.getRgstUserId())); // 등록 id로 프로필 url 찾아 넣기
            postMap.put("nickNm", columnValueMapper.mbrIdToNickNm(post.getRgstUserId())); // 등록 id로 nickNm 찾아 넣기(없으면 null)
            // ObjectMapper를 사용하여 맵을 JSON으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(postMap);
        }else {
            // Optional이 비어있는 경우 처리
            throw new NoSuchElementException("해당 게시글이 존재하지 않습니다.");
        }


    }

    public Map<String,Object> bbsReadPostFiles(long pstId) {
        Optional<MvcBbs> postInfoOptional = mvcBbsRepository.findByPstIdAndDeltYn(pstId, "N");

        if (postInfoOptional.isPresent()) {
            MvcBbs postInfo = postInfoOptional.get(); // 글 정보 가져오기
            long fileId = postInfo.getAtchFileid();
            List<MvcAtchFile> filesList = mvcAtchFileRepository.findAllByAtchFileIdCkAtchFileIdOrderByAtchFileIdCkSeqAsc(fileId); // 파일 id로 파일 리스트 검색
            int seq = 0;
            List<Map<String, Object>> atchFileList = new ArrayList<>(); // 파일 정보 넣을 리스트 만들기
            Map<String, Object> atchFileMap = new HashMap<>();
            String s3Link = "https://mvc0605bucket.s3.ap-northeast-2.amazonaws.com/";

            for (MvcAtchFile file : filesList) {
                Map<String, Object> atchFileInfo = new HashMap<>(); // 파일 정보 넣을 맵 만들기
                atchFileInfo.put("fileId", fileId);
                atchFileInfo.put("seq", seq++);
                atchFileInfo.put("fileName", fileUtils.removeFileExtension(file.getActlFileNm())); // 확장자 떼서 파일이름으로 추가
                atchFileInfo.put("fileExtn", "." + file.getStrgFileExtn());
                atchFileInfo.put("fileUrl", s3Link + file.getStrgFilePath() + "/" + file.getStrgFileNm() + "." + file.getStrgFileExtn());
                atchFileList.add(atchFileInfo);
            }
            atchFileMap.put("total", atchFileList.size());
            atchFileMap.put("data", atchFileList);
            return atchFileMap;
        } else {
            // postInfoOptional이 비어 있는 경우, 즉 검색 결과가 없는 경우
            Map<String, Object> emptyMap = new HashMap<>();
            emptyMap.put("total", 0);
            emptyMap.put("data", new ArrayList<>());
            return emptyMap;
        }
    }
}
