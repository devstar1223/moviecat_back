package com.moviecat.www.util;

import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.repository.MvcMbrInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ColumnValueMapper {

    private final MvcMbrInfoRepository mvcMbrInfoRepository;
    // 회원 ID -> 회원 이름
    public String mbrIdToMbrNm(String mbrId) {
        Optional<MvcMbrInfo> mbrNmOptional = mvcMbrInfoRepository.findByMbrId(mbrId);
        if(mbrNmOptional.isPresent()){
            return mbrNmOptional.get().getMbrNm();
        }
        else {
            throw new NoSuchElementException("회원 정보가 유효하지 않습니다.");
        }
    }

}
