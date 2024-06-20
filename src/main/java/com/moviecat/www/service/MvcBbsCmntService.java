package com.moviecat.www.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moviecat.www.dto.MvcCmntDto;
import com.moviecat.www.entity.MvcBbsCmnt;
import com.moviecat.www.repository.MvcBbsCmntRepository;
import com.moviecat.www.util.ColumnValueMapper;
import com.moviecat.www.util.TimeFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MvcBbsCmntService {

    private final MvcBbsCmntRepository mvcBbsCmntRepository;
    private final ColumnValueMapper columnValueMapper;
    private final TimeFormat timeFormat;

    @Transactional
    public void bbsWriteCmnt(MvcCmntDto mvcCmntDto){
        Optional<MvcBbsCmnt> cmntGroupOptional = mvcBbsCmntRepository.findTopByPstIdOrderByCmntGroupDesc(mvcCmntDto.getPstId());
        int group = cmntGroupOptional.map(cmnt -> cmnt.getCmntGroup() + 1).orElse(0);

        Optional<MvcBbsCmnt> cmntSeqOptional = mvcBbsCmntRepository.findTopByPstIdAndCmntGroupOrderBySeqDesc(mvcCmntDto.getPstId(),group);
        int seq = cmntSeqOptional.map(cmnt -> cmnt.getSeq() + 1).orElse(0);

        String mbrNm = columnValueMapper.mbrIdToMbrNm(mvcCmntDto.getCmntMbrId()); // mbrId 넣고 mbrNm으로 받기

        MvcBbsCmnt newCmnt = new MvcBbsCmnt();
        newCmnt.setPstId(mvcCmntDto.getPstId());
        newCmnt.setUpCmntId(0);
        newCmnt.setCmntLyr(0);
        newCmnt.setCmntGroup(group);
        newCmnt.setCmntMbrId(mvcCmntDto.getCmntMbrId());
        newCmnt.setCmntMbrNickNm(mvcCmntDto.getCmntMbrNickNm());
        newCmnt.setSeq(seq);
        newCmnt.setCn(mvcCmntDto.getCn());
        newCmnt.setRgstUserId(mvcCmntDto.getMbrId());
        newCmnt.setRgstUserNm(mbrNm);
        newCmnt.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
        newCmnt.setMdfcnUserId(mvcCmntDto.getMbrId());
        newCmnt.setMdfcnUserNm(mbrNm);
        newCmnt.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
        newCmnt.setDeltYn('N');
        mvcBbsCmntRepository.save(newCmnt);
    }

    @Transactional
    public void bbsWriteReply(MvcCmntDto mvcCmntDto) {
        MvcBbsCmnt newReply = new MvcBbsCmnt();
        newReply.setPstId(mvcCmntDto.getPstId());
        newReply.setUpCmntId(mvcCmntDto.getUpCmntId());
        newReply.setCmntLyr(mvcCmntDto.getCmntLyr());
        newReply.setCmntGroup(mvcCmntDto.getCmntGroup());
        newReply.setCmntMbrId(mvcCmntDto.getCmntMbrId());
        newReply.setCmntMbrNickNm(mvcCmntDto.getCmntMbrNickNm());
        newReply.setSeq(mvcCmntDto.getSeq());
        newReply.setCn(mvcCmntDto.getCn());
        newReply.setRgstUserId(mvcCmntDto.getCmntMbrId());
        newReply.setRgstUserNm(mvcCmntDto.getMbrNm());
        newReply.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
        newReply.setMdfcnUserId(mvcCmntDto.getCmntMbrId());
        newReply.setMdfcnUserNm(mvcCmntDto.getMbrNm());
        newReply.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
        newReply.setDeltYn('N');
        mvcBbsCmntRepository.save(newReply);
    }

    @Transactional
    public void bbsDeleteCmnt(MvcCmntDto mvcCmntDto) {
        Optional<MvcBbsCmnt> deleteCmntOptional = mvcBbsCmntRepository.findById(mvcCmntDto.getCmntId());
        String mbrNm = columnValueMapper.mbrIdToMbrNm(mvcCmntDto.getCmntMbrId()); // mbrId 넣고 mbrNm으로 받기
        if (deleteCmntOptional.isPresent()) {
            MvcBbsCmnt deleteCmnt = deleteCmntOptional.get();
            deleteCmnt.setDeltYn('Y');
            deleteCmnt.setMdfcnUserId(mvcCmntDto.getCmntMbrId());
            deleteCmnt.setMdfcnUserNm(mbrNm);
            deleteCmnt.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
            mvcBbsCmntRepository.save(deleteCmnt);
        } else {
            throw new IllegalArgumentException("해당 댓글을 찾을 수 없습니다");
        }
    }

    @Transactional
    public void bbsEditCmnt(MvcCmntDto mvcCmntDto){
        Optional<MvcBbsCmnt> editCmntOptional = mvcBbsCmntRepository.findByCmntIdAndDeltYn(mvcCmntDto.getCmntId(),'N');
        String mbrNm = columnValueMapper.mbrIdToMbrNm(mvcCmntDto.getCmntMbrId()); // mbrId 넣고 mbrNm으로 받기
        if (editCmntOptional.isPresent()) {
            MvcBbsCmnt editCmnt = editCmntOptional.get();
            editCmnt.setCn(mvcCmntDto.getCn());
            editCmnt.setMdfcnUserId(mvcCmntDto.getCmntMbrId());
            editCmnt.setMdfcnUserNm(mbrNm);
            editCmnt.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
            mvcBbsCmntRepository.save(editCmnt);
        } else {
            throw new IllegalArgumentException("해당 댓글을 찾을 수 없습니다");
        }
    }

    @Transactional
    public String bbsReadCmnt(long pstId) throws JsonProcessingException {
        List<MvcBbsCmnt> cmntList = mvcBbsCmntRepository.findByPstIdAndDeltYnOrderByCmntGroupAscSeqAsc(pstId, 'N');
        List<Map<String, Object>> dataList = new ArrayList<>();

        int total = cmntList.size();

        for (MvcBbsCmnt cmnt : cmntList) {
            Map<String, Object> cmntData = new LinkedHashMap<>();
            cmntData.put("cmntId",cmnt.getCmntId());
            cmntData.put("seq", cmnt.getSeq());
            cmntData.put("cmntGroup", cmnt.getCmntGroup());
            cmntData.put("cmntLyr", cmnt.getCmntLyr());
            System.out.println(cmnt.getUpCmntId());
            Optional<MvcBbsCmnt> upCmntNickNmOptional = mvcBbsCmntRepository.findById(cmnt.getUpCmntId());
            String upCmntNickNmValue = null;
            if (upCmntNickNmOptional.isPresent()) {
                MvcBbsCmnt upCmntNickNm = upCmntNickNmOptional.get();
                if (upCmntNickNm.getUpCmntId() != 0) {
                    upCmntNickNmValue = upCmntNickNm.getCmntMbrNickNm();
                }
            }
            cmntData.put("upCmntNickNm", upCmntNickNmValue);
            cmntData.put("mvcId",columnValueMapper.mbrIdToMvcId(cmnt.getRgstUserId()));
            cmntData.put("profileUrl",columnValueMapper.mbrIdToAtchFileUrl(cmnt.getRgstUserId()));
            cmntData.put("nickNm", cmnt.getCmntMbrNickNm());
            cmntData.put("cn", cmnt.getCn());
            String rgstTime = timeFormat.formatDate(cmnt.getRgstDay());
            cmntData.put("rgstDay", rgstTime);
            dataList.add(cmntData);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("data", dataList);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(result);
    }
}
