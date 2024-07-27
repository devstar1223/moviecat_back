package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.entity.MvcScrBbs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MvcScrBbsRepository extends JpaRepository<MvcScrBbs, Long> {
    Optional<MvcScrBbs> findByScrIdAndDeltYn(long scrId,String deltYn);
    List<MvcScrBbs> findByMenuIdAndDeltYnOrderByScrIdDesc(long menuId, String deltYn);
    List<MvcScrBbs> findByDeltYnOrderByScrIdDesc(String deltYn);
    List<MvcScrBbs> findByVdoNmContainingOrderByRgstDayDesc(String vdoNm);
    List<MvcScrBbs> findByRgstUserIdAndDeltYnOrderByRgstDayDesc(String rgstUserId, String deltYn);

    List<MvcScrBbs> findByDeltYnAndVdoNmContainingOrVdoNmEnContainingOrderByRgstDayDesc(String deltYn, String vdoNm, String vdoNmEn);

    @Query(
            "SELECT b.scrId AS scrId, " +
                    "       b.vdoNm AS vdoNm, " +
                    "       b.vdoNmEn AS vdoNmEn, " +
                    "       b.opngYear AS opngYear, " +
                    "       b.scr AS scr, " +
                    "       b.rgstDay AS rgstDay, " +
                    "       b.vdoEvl AS vdoEvl, " +
                    "       b.nickNm AS nickNm, " +
                    "       COALESCE(likeCounts.count, 0) AS likeCnt, " +
                    "       COALESCE(likeYn.yn, 'N') AS likeYn " +
                    "FROM MvcScrBbs b " +
                    "LEFT JOIN ( " +
                    "    SELECT r.menuId AS menuId, " +
                    "           r.rcmdtnSeId AS scrId, " +
                    "           COUNT(r) AS count " +
                    "    FROM MvcRcmdtnInfo r " +
                    "    WHERE r.deltYn = 'N' " +
                    "    GROUP BY r.menuId, r.rcmdtnSeId " +
                    ") likeCounts " +
                    "ON b.scrId = likeCounts.scrId AND b.menuId = likeCounts.menuId " +
                    "LEFT JOIN ( " +
                    "    SELECT r.menuId AS menuId, " +
                    "           r.rcmdtnSeId AS scrId, " +
                    "           'Y' AS yn " +
                    "    FROM MvcRcmdtnInfo r " +
                    "    WHERE r.deltYn = 'N' AND r.rgstUserId = :userId " +
                    "    GROUP BY r.menuId, r.rcmdtnSeId, r.rgstUserId " +
                    ") likeYn " +
                    "ON b.scrId = likeYn.scrId AND b.menuId = likeYn.menuId " +
                    "WHERE b.deltYn = 'N' " +
                    "AND b.menuId = :menuId " +
                    "ORDER BY b.rgstDay DESC"
    )
    Page<Object[]> findWithCountsAndLikeYnByUserId(@Param("userId") String userId, @Param("menuId") long menuId,Pageable pageable);
}
