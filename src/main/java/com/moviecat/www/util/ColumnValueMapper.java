package com.moviecat.www.util;

import com.moviecat.www.entity.MvcBbsCmnt;
import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.entity.MvcRcmdtnInfo;
import com.moviecat.www.repository.MvcBbsCmntRepository;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import com.moviecat.www.repository.MvcRcmdtnInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ColumnValueMapper {

    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    private final MvcRcmdtnInfoRepository mvcRcmdtnInfoRepository;
    private final MvcBbsCmntRepository mvcBbsCmntRepository;
    // 회원 ID -> 회원 이름
    public String mbrIdToMbrNm(String mbrId) { // mbrId 주면, 실명 반환
        Optional<MvcMbrInfo> mbrNmOptional = mvcMbrInfoRepository.findByMbrId(mbrId);
        if(mbrNmOptional.isPresent()){
            return mbrNmOptional.get().getMbrNm();
        }
        else {
            throw new NoSuchElementException("회원 정보가 유효하지 않습니다.");
        }
    }

    public String mbrIdToNickNm(String mbrId) { // mbrId 주면, 닉네임 반환.
        Optional<MvcMbrInfo> mbrNmOptional = mvcMbrInfoRepository.findByMbrId(mbrId);
        if(mbrNmOptional.isPresent()){
            return mbrNmOptional.get().getNickNm();
        }
        else {
            throw new NoSuchElementException("회원 정보가 유효하지 않습니다.");
        }
    }

    public String mbrIdToAtchFileUrl(String mbrId) { // mbrId 주면, 프로필 url 주소 반환
        Optional<MvcMbrInfo> mbrNmOptional = mvcMbrInfoRepository.findByMbrId(mbrId);
        if(mbrNmOptional.isPresent()){
            return mbrNmOptional.get().getAtchFileUrl();
        }
        else {
            return null;
        }
    }

    public int pstIdAndMenuIdToRcmdTotal(long pstId, long menuId) { // 게시글 id와 메뉴 id 주면, 좋아요 수 반환
        List<MvcRcmdtnInfo> rcmdList = mvcRcmdtnInfoRepository.findByRcmdtnSeIdAndMenuIdAndDeltYn(pstId, menuId, "N"); // 해당되는 추천 리스트로 받아와서
        return rcmdList.size(); // 사이즈 만큼 좋아요 수 반환
    }

    public String mbrIdToRcmdDeltYn(String mbrId){ // mbrId를 받으면, 해당 유저가 상세글 보기에서 좋아요를 눌렀는지 안눌렀는지
        Optional<MvcRcmdtnInfo> rcmdOptional = mvcRcmdtnInfoRepository.findByRgstUserIdAndDeltYn(mbrId, "N");
        if(rcmdOptional.isPresent()){
            return "N";
        }
        else{
            return "Y";
        }
    }

    public int pstIdToCmntTotal(long pstId) { // 게시글 id주면, 댓글 갯수 반환
        List<MvcBbsCmnt> rcmdList = mvcBbsCmntRepository.findByPstIdAndDeltYn(pstId,'N'); // 해당되는 추천 리스트로 받아와서
        return rcmdList.size(); // 사이즈 만큼 좋아요 수 반환
    }

}
