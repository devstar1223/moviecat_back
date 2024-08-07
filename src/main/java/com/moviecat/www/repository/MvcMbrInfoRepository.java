package com.moviecat.www.repository;

import com.moviecat.www.dto.MvcMbrInfoDto;
import com.moviecat.www.entity.MvcMbrInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MvcMbrInfoRepository extends JpaRepository<MvcMbrInfo, Long> {

    Optional<MvcMbrInfo> findByMvcId(long mvcId);

    Optional<MvcMbrInfo> findByMbrId(String mbrId);
    Optional<MvcMbrInfo> findByRgstUserId(String rgstUserId);
    Optional<MvcMbrInfo> findByNickNm(String nickNm);
    Optional<MvcMbrInfo> findByMbrNmAndEmail(String mbrNm, String email);
    Optional<MvcMbrInfo> findByMbrIdAndMbrNmAndEmail(String mbrId, String mbrNm, String email);
}
