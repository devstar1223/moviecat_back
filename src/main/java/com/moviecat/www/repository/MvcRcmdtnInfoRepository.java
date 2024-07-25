package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcRcmdtnInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MvcRcmdtnInfoRepository extends JpaRepository<MvcRcmdtnInfo, Long> {
    Optional<MvcRcmdtnInfo> findByMenuIdAndRcmdtnSeIdAndRgstUserId(int menuId, long rcmdtnSeId, String rgstUserId);
    List<MvcRcmdtnInfo> findByRcmdtnSeIdAndMenuIdAndDeltYn(long rcmdtnSeId, long menuId, String Yn); // 게시글 id, 메뉴 id, yn
    List<MvcRcmdtnInfo> findByDeltYnAndMenuIdAndRcmdtnSeId(String deltYn, long menuId, long rcmdtnSeId);
    Optional<MvcRcmdtnInfo> findByMenuIdAndRcmdtnSeIdAndRgstUserIdAndDeltYn(long menuId,long rcmdtnSeId,String rgstUserId, String deltYn);

    @Query("SELECT COUNT(m) FROM MvcRcmdtnInfo m WHERE m.deltYn = :deltYn AND m.menuId = :menuId AND m.rcmdtnSeId = :rcmdtnSeId")
    Long totalRcmdtn(@Param("deltYn") String deltYn,
                           @Param("menuId") Long menuId,
                           @Param("rcmdtnSeId") Long rcmdtnSeId);
}
