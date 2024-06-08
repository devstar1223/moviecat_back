package com.moviecat.www.repository;

import com.moviecat.www.dto.MvcMbrInfoDto;
import com.moviecat.www.entity.MvcMbrInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MvcMbrInfoRepository extends JpaRepository<MvcMbrInfo, Long> {
    Optional<MvcMbrInfo> findByMbrId(String mbrId);
    Optional<MvcMbrInfo> findByNickNm(String nickNm);
    Optional<MvcMbrInfo> findByMbrNmAndEmail(String mbrNm, String email);
}
