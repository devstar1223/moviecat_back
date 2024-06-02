package com.moviecat.www.service;

import com.moviecat.www.dto.MvcBbsDto;
import com.moviecat.www.entity.MvcBbs;
import com.moviecat.www.repository.MvcBbsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MvcBbsService {
    private final MvcBbsRepository mvcBbsRepository;

    public void bbsWritePost(MvcBbsDto mvcBbsDto) {
        MvcBbs newPost = new MvcBbs();
        newPost.setMenuId(mvcBbsDto.getMenuId());
        newPost.setTtl(mvcBbsDto.getTtl());
        newPost.setCn(mvcBbsDto.getCn());
        newPost.setAtchFileId(mvcBbsDto.getAtchFileId());
        newPost.setSpoYn(mvcBbsDto.getSpoYn());
        newPost.setRgstUserId(mvcBbsDto.getRgstUserId());
        newPost.setRgstUserNm(mvcBbsDto.getRgstUserNm());
        newPost.setRgstDay(Timestamp.valueOf(LocalDateTime.now())); // 현재시간
        newPost.setMdfcnUserId(mvcBbsDto.getMdfcnUserId());
        newPost.setMdfcnUserNm(mvcBbsDto.getMdfcnUserNm());
        newPost.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 현재시간 (수정 api 따로)
        newPost.setDeltYn("N"); // 글 쓰기 이므로, 기본적으로 "N"으로 설정
        mvcBbsRepository.save(newPost);
    }

    public void bbsEditPost(MvcBbsDto newPostDto) {
        Optional<MvcBbs> bbsOptional = mvcBbsRepository.findById(newPostDto.getPstId()); // 받아온 정보로 글 찾기
        MvcBbs post = bbsOptional.get();
        post.setTtl(newPostDto.getTtl());
        post.setCn(newPostDto.getCn());
        post.setAtchFileId(newPostDto.getAtchFileId());
        post.setSpoYn(newPostDto.getSpoYn());
        post.setMdfcnUserId(newPostDto.getMdfcnUserId()); // 들어온 데이터의 id로 수정 id 등록(admin 일부 고려)
        post.setMdfcnUserNm(newPostDto.getMdfcnUserNm()); // 들어온 데이터의 이름으로 수정자 등록(admin 일부 고려)
        post.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 현재 시간
        mvcBbsRepository.save(post);
    }

    public void bbsDeletePost(MvcBbsDto newPostDto) {
        Optional<MvcBbs> bbsOptional = mvcBbsRepository.findById(newPostDto.getPstId()); // 받아온 정보로 글 찾기
        MvcBbs post = bbsOptional.get();
        post.setMdfcnUserId(newPostDto.getMdfcnUserId()); // 들어온 데이터의 id로 수정 id 등록(admin 일부 고려)
        post.setMdfcnUserNm(newPostDto.getMdfcnUserNm()); // 들어온 데이터의 이름으로 수정자 등록(admin 일부 고려)
        post.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 현재 시간
        post.setDeltYn("Y"); // 글 삭제 이므로, "Y"로 설정
        mvcBbsRepository.save(post);
    }
}
