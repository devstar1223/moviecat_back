package com.moviecat.www.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviecat.www.dto.MvcBbsDto;
import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.repository.MvcBbsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MvcBbsService {
    private final MvcBbsRepository mvcBbsRepository;

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
        post.setMdfcnUserId(newPostDto.getRgstUserId()); // 들어온 데이터의 id로 수정 id 등록
        post.setMdfcnUserNm(newPostDto.getRgstUserNm()); // 들어온 데이터의 이름으로 수정자 등록
        post.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 현재 시간
        mvcBbsRepository.save(post);
    }

    @Transactional
    public void bbsDeletePost(MvcBbsDto newPostDto) {
        Optional<MvcBbs> bbsOptional = mvcBbsRepository.findById(newPostDto.getPstId()); // 받아온 정보로 글 찾기
        MvcBbs post = bbsOptional.get();
        post.setMdfcnUserId(newPostDto.getMdfcnUserId()); // 들어온 데이터의 id로 수정 id 등록(admin 일부 고려)
        post.setMdfcnUserNm(newPostDto.getMdfcnUserNm()); // 들어온 데이터의 이름으로 수정자 등록(admin 일부 고려)
        post.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 현재 시간
        post.setDeltYn("Y"); // 글 삭제 이므로, "Y"로 설정
        mvcBbsRepository.save(post);
    }

    @Transactional
    public String bbsReadBoard(long boardId) throws JsonProcessingException {
        List<MvcBbs> postList = mvcBbsRepository.findByMenuIdAndDeltYnOrderByPstIdAsc(boardId, "N");

        // Jackson ObjectMapper를 사용하여 List<MvcBbs>를 JSON 형식의 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPostList = objectMapper.writeValueAsString(postList);

        return jsonPostList;
    }

//    public String bbsReadPost(long pstId) throws JsonProcessingException {
//        Optional<MvcBbs> postOptional = mvcBbsRepository.findById(pstId);
//
//        if(postOptional.isPresent()){
//            MvcBbs post = postOptional.get();
//
//        }
//
//
//    }
}
