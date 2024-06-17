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
    public String bbsReadBoard(long boardId, int page) throws JsonProcessingException {
        List<MvcBbs> postList = mvcBbsRepository.findByMenuIdAndDeltYnOrderByPstIdAsc(boardId, "N");
        List<MvcBbs> pagedPostList;
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

    public String bbsReadPost(long menuId,long pstId) throws JsonProcessingException {
        Optional<MvcBbs> postOptional = mvcBbsRepository.findByMenuIdAndPstIdAndDeltYn(menuId,pstId,"N");

        if(postOptional.isPresent()){
            MvcBbs post = postOptional.get();
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("ttl", post.getTtl());
            postMap.put("cn", post.getCn());
            postMap.put("spoYn", post.getSpoYn());

            String rgstTime = timeFormat.formatDate(post.getRgstDay());
            postMap.put("rgstDate", rgstTime);

            List<MvcRcmdtnInfo> rcmdList = mvcRcmdtnInfoRepository.findByRcmdtnSeIdAndRcmdtnSeAndDeltYn(post.getMenuId(), post.getPstId(), "N"); // 해당되는 추천 리스트로 받아와서
            postMap.put("rcmd", rcmdList.size()); // 사이즈 만큼 좋아요 수 할당

            Optional<MvcMbrInfo> mbrInfoOptional = mvcMbrInfoRepository.findByRgstUserId(post.getRgstUserId()); // 등록id로 유저 찾아오기
            MvcMbrInfo mbrInfo = mbrInfoOptional.get();
            postMap.put("profileUrl", mbrInfo.getAtchFileUrl()); // url 찾아 넣기 (없으면 null 넣음)
            postMap.put("nickNm", mbrInfo.getNickNm()); // nickNm 찾아 넣기

            // ObjectMapper를 사용하여 맵을 JSON으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(postMap);
        }else {
            // Optional이 비어있는 경우 처리
            throw new NoSuchElementException("해당 게시글이 존재하지 않습니다.");
        }


    }

    public List<String> bbsReadPostFiles(long menuId, long pstId) {
        Optional<MvcBbs> fileIdOptional = mvcBbsRepository.findByPstIdAndDeltYn(pstId, "N");

        if (fileIdOptional.isPresent()) {
            Long fileId = fileIdOptional.get().getAtchFileid();
            List<MvcAtchFile> filesList = mvcAtchFileRepository.findAllByAtchFileIdCkAtchFileIdOrderByAtchFileIdCkSeqAsc(fileId);
            List<String> atchFileList = new ArrayList<>();

            for (MvcAtchFile file : filesList) {
                String s3Link = "https://mvc0605bucket.s3.ap-northeast-2.amazonaws.com/";
                atchFileList.add(s3Link+file.getStrgFilePath()+"/"+file.getStrgFileNm()+"."+file.getStrgFileExtn());
            }
            return atchFileList;
        } else {
            // fileIdOptional이 비어 있는 경우, 즉 검색 결과가 없는 경우
            return new ArrayList<>();
        }
    }
}
