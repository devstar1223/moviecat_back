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
        Optional<MvcBbsCmnt> cmntSeqOptional = mvcBbsCmntRepository.findTopByPstIdOrderBySeqDesc(mvcCmntDto.getPstId());
        int seq = cmntSeqOptional.map(cmnt -> cmnt.getSeq() + 1).orElse(0);

        Optional<MvcBbsCmnt> cmntGroupOptional = mvcBbsCmntRepository.findTopByPstIdOrderByCmntGroupDesc(mvcCmntDto.getPstId());
        int group = cmntGroupOptional.map(cmnt -> cmnt.getCmntGroup() + 1).orElse(0);

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
        newCmnt.setRgstUserId(mvcCmntDto.getCmntMbrId());
        newCmnt.setRgstUserNm(mbrNm);
        newCmnt.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
        newCmnt.setMdfcnUserId(mvcCmntDto.getCmntMbrId());
        newCmnt.setMdfcnUserNm(mbrNm);
        newCmnt.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
        newCmnt.setDeltYn('N');
        mvcBbsCmntRepository.save(newCmnt);
    }

    @Transactional
    public void bbsWriteReply(MvcCmntDto mvcCmntDto){
        long upCmntId = mvcCmntDto.getUpCmntId(); // 상위댓글 id
        Optional<MvcBbsCmnt> upCmntOptional = mvcBbsCmntRepository.findById(upCmntId); // 상위댓글 id로, 상위댓글 정보 찾기
        int group;
        int lyr;
        int seq;
        if (upCmntOptional.isPresent()) {
            MvcBbsCmnt upCmnt = upCmntOptional.get();
            group = upCmnt.getCmntGroup();
            lyr = upCmnt.getCmntLyr() + 1;
            Optional<MvcBbsCmnt> seqOptional = mvcBbsCmntRepository.findTopByUpCmntIdOrderBySeqDesc(upCmntId);
            seq = seqOptional.map(cmnt -> cmnt.getSeq() + 1).orElse(upCmnt.getSeq() + 1);
        } else {
            throw new NoSuchElementException("답글을 작성할 상위 댓글을 찾을 수 없습니다.");
        }

        List<MvcBbsCmnt> cmntsToUpdate = mvcBbsCmntRepository.findByPstIdAndSeqGreaterThanEqual(mvcCmntDto.getPstId(), seq); // 게시글에서, 이 답글보다 순서 같거나 큰거 검색
        for (MvcBbsCmnt cmnt : cmntsToUpdate) {
            cmnt.setSeq(cmnt.getSeq() + 1); // 전부 순서 +1씩 해주고
            mvcBbsCmntRepository.save(cmnt); // 저장
        }

        String mbrNm = columnValueMapper.mbrIdToMbrNm(mvcCmntDto.getCmntMbrId()); // mbrId 넣고 mbrNm으로 받기

        MvcBbsCmnt newReply = new MvcBbsCmnt();
        newReply.setPstId(mvcCmntDto.getPstId());
        newReply.setUpCmntId(mvcCmntDto.getUpCmntId());
        newReply.setCmntLyr(lyr);
        newReply.setCmntGroup(group);
        newReply.setCmntMbrId(mvcCmntDto.getCmntMbrId());
        newReply.setCmntMbrNickNm(mvcCmntDto.getCmntMbrNickNm());
        newReply.setSeq(seq);
        newReply.setCn(mvcCmntDto.getCn());
        newReply.setRgstUserId(mvcCmntDto.getCmntMbrId());
        newReply.setRgstUserNm(mbrNm);
        newReply.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
        newReply.setMdfcnUserId(mvcCmntDto.getCmntMbrId());
        newReply.setMdfcnUserNm(mbrNm);
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
        List<MvcBbsCmnt> cmntList = mvcBbsCmntRepository.findByPstIdAndDeltYnOrderBySeqAsc(pstId, 'N');
        List<Map<String, Object>> dataList = new ArrayList<>();

        int total = cmntList.size();

        for (MvcBbsCmnt cmnt : cmntList) {
            Map<String, Object> cmntData = new LinkedHashMap<>();
            cmntData.put("seq", cmnt.getSeq());
            cmntData.put("cmntGroup", cmnt.getCmntGroup());
            cmntData.put("cmntLyr", cmnt.getCmntLyr());
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
