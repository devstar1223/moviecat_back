package com.moviecat.www.service;

import com.moviecat.www.dto.MvcScrBbsDto;
import com.moviecat.www.entity.MvcScrBbs;
import com.moviecat.www.repository.MvcScrBbsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MvcScrBbsService {

    private final MvcScrBbsRepository mvcScrBbsRepository;

//    private String vdoNm; // 영화명 C
//    private Timestamp opngDay; // 개봉일 C
//    private int scr; // 평점 C
//    private String vdoEvl; // 영화평 C
    public void scrBbsWrite(MvcScrBbsDto mvcScrBbsDto){
        MvcScrBbs newScr = new MvcScrBbs();
        newScr.setMenuId(mvcScrBbsDto.getMenuId());
        newScr.setVdoNm(mvcScrBbsDto.getVdoNm());
        newScr.setOpngDay(mvcScrBbsDto.getOpngDay());
        newScr.setScr(mvcScrBbsDto.getScr());
        newScr.setVdoEvl(mvcScrBbsDto.getVdoEvl());
        newScr.setRgstUserId(mvcScrBbsDto.getMbrId());
        newScr.setRgstUserNm(mvcScrBbsDto.getMbrNm());
        newScr.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
        newScr.setMdfcnUserId(mvcScrBbsDto.getMbrId());
        newScr.setMdfcnUserNm(mvcScrBbsDto.getMbrNm());
        newScr.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
        newScr.setDeltYn("N");
        mvcScrBbsRepository.save(newScr);
    }
}
