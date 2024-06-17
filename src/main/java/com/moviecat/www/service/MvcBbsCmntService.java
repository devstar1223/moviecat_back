package com.moviecat.www.service;

import com.moviecat.www.dto.MvcCmntDto;
import com.moviecat.www.entity.MvcBbsCmnt;
import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.repository.MvcBbsCmntRepository;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MvcBbsCmntService {
    private final MvcBbsCmntRepository mvcBbsCmntRepository;
    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    @Transactional
    public void bbsWriteCmnt(MvcCmntDto mvcCmntDto){
        Optional<MvcBbsCmnt> cmntSeqOptional = mvcBbsCmntRepository.findTopByPstIdOrderBySeqDesc(mvcCmntDto.getPstId());
        Optional<MvcBbsCmnt> cmntGroupOptional = mvcBbsCmntRepository.findTopByPstIdOrderByCmntGroupDesc(mvcCmntDto.getPstId());
        Optional<MvcMbrInfo> mbrNmOptional = mvcMbrInfoRepository.findByMbrId(mvcCmntDto.getCmntMbrId());
        int seq = cmntSeqOptional.map(cmnt -> cmnt.getSeq() + 1).orElse(0);
        int group = cmntGroupOptional.map(cmnt -> cmnt.getCmntGroup() + 1).orElse(0);
        String mbrNm = mbrNmOptional.map(MvcMbrInfo::getMbrNm)
                .orElseThrow(() -> new RuntimeException("회원 정보가 유효하지 않습니다."));
        MvcBbsCmnt newCmnt = new MvcBbsCmnt();
        newCmnt.setPstId(mvcCmntDto.getPstId());
        newCmnt.setUpCmntId(0);
        newCmnt.setCmntLyr(0);
        newCmnt.setCmntGroup(group);
        newCmnt.setCmntMbrId(mvcCmntDto.getCmntMbrId());
        newCmnt.setCmntMbrNickNm(mvcCmntDto.getCmntMbrNickNm());
        newCmnt.setSeq(seq);
        newCmnt.setCn(mvcCmntDto.getCn());
        newCmnt.setRgstUserId(mvcCmntDto.getCmntMbrId());
        newCmnt.setRgstUserNm(mbrNm);
        newCmnt.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
        newCmnt.setMdfcnUserId(mvcCmntDto.getCmntMbrId());
        newCmnt.setMdfcnUserNm(mbrNm);
        newCmnt.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
        newCmnt.setDeltYn('N');
        mvcBbsCmntRepository.save(newCmnt);
    }
}
