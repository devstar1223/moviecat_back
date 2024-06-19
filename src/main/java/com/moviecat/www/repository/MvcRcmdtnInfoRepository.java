package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcRcmdtnInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MvcRcmdtnInfoRepository extends JpaRepository<MvcRcmdtnInfo, Long> {
    Optional<MvcRcmdtnInfo> findByMenuIdAndRcmdtnSeIdAndRgstUserId(int menuId, long rcmdtnSeId, String rgstUserId);

    List<MvcRcmdtnInfo> findByRcmdtnSeIdAndMenuIdAndDeltYn(long rcmdtnSeId, long menuId, String Yn); // 게시글 id, 메뉴 id, yn

    List<MvcRcmdtnInfo> findByDeltYnAndMenuIdAndRcmdtnSeId(String deltYn, long menuId, long rcmdtnSeId);

    Optional<MvcRcmdtnInfo> findByMenuIdAndRcmdtnSeIdAndRgstUserIdAndDeltYn(long menuId,long rcmdtnSeId,String rgstUserId, String deltYn);
}
