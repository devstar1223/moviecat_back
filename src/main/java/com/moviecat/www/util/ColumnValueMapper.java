package com.moviecat.www.util;

import com.moviecat.www.entity.*;
import com.moviecat.www.repository.*;
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
    private final MvcBbsRepository mvcBbsRepository;
    private final MvcMenuRepository mvcMenuRepository;
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

    public int pstIdToCmntTotal(long pstId) { // 게시글 id주면, 댓글 갯수 반환
        List<MvcBbsCmnt> rcmdList = mvcBbsCmntRepository.findByPstIdAndDeltYn(pstId,'N'); // 해당되는 추천 리스트로 받아와서
        return rcmdList.size(); // 사이즈 만큼 좋아요 수 반환
    }

    public long mbrIdToMvcId(String mbrId) { // mbrId 주면, mvcId 반환
        Optional<MvcMbrInfo> mvcIdOptional = mvcMbrInfoRepository.findByMbrId(mbrId); // 해당되는 추천 리스트로 받아와서
        if(mvcIdOptional.isPresent()){
            return mvcIdOptional.get().getMvcId();
        }
        else {
            throw new NoSuchElementException("회원 정보가 유효하지 않습니다.");
        }
    }

    public long pstIdToMenuId(long pstId){
        Optional<MvcBbs> menuIdOptional = mvcBbsRepository.findById(pstId);
        if(menuIdOptional.isPresent()){
            return menuIdOptional.get().getMenuId();
        }
        else {
            throw new NoSuchElementException("회원 정보가 유효하지 않습니다.");
        }
    }

    public String menuIdToMenuNm(long menuId){
        Optional<MvcMenu> mvcMenuNmOptional = mvcMenuRepository.findById(menuId);
        if(mvcMenuNmOptional.isPresent()){
            return mvcMenuNmOptional.get().getMenuNm();
        }
        else {
            throw new NoSuchElementException("메뉴 정보가 유효하지 않습니다.");
        }
    }

}
