package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcRcmdtnInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MvcRcmdtnInfoRepository extends JpaRepository<MvcRcmdtnInfo, Long> {
    Optional<MvcRcmdtnInfo> findByRcmdtnSeAndRcmdtnSeIdAndRgstUserId(int rcmdtnSe, long rcmdtnSeId, String rgstUserId);
}
