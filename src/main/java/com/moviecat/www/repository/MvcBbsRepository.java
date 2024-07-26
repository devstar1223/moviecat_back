package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcBbs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface MvcBbsRepository extends JpaRepository<MvcBbs, Long> {
    List<MvcBbs> findByMenuIdAndDeltYnOrderByPstIdDesc(Long menuId, String deltYn);
    Optional<MvcBbs> findByMenuIdAndPstIdAndDeltYn(long menuId, long pstId, String deltYn);
    Optional<MvcBbs> findByPstIdAndDeltYn(long pstId, String deltYn);
    List<MvcBbs> findByRgstUserIdAndDeltYnOrderByRgstDayDesc(String rgstUserId, String deltYn);

    List<MvcBbs> findByMenuIdAndDeltYnOrderByRgstDayDesc(long menuId, String deltYn);

    // 제목(ttl) 기준 - 게시판 검색
    List<MvcBbs> findByMenuIdAndDeltYnAndTtlContainingOrderByRgstDayDesc(Long menuId, String deltYn, String ttl);
    // 제목(ttl) 기준 - 전체 검색
    List<MvcBbs> findByDeltYnAndTtlContainingOrderByRgstDayDesc(String deltYn, String ttl);

    // 제목(ttl) 또는 내용(cn) 기준 - 게시판 검색
    List<MvcBbs> findByMenuIdAndDeltYnAndTtlContainingOrMenuIdAndDeltYnAndCnContainingOrderByRgstDayDesc(Long menuId, String deltYn, String word, Long menuId2, String deltYn2, String word2);
    // 제목(ttl) 또는 내용(cn) 기준 - 전체 검색
    List<MvcBbs> findByDeltYnAndTtlContainingOrDeltYnAndCnContainingOrderByRgstDayDesc(String deltYn, String word, String deltYn2, String word2);


    // 작성자(rgstUserId) 기준 - 게시판 검색
    List<MvcBbs> findByMenuIdAndDeltYnAndRgstUserIdOrderByRgstDayDesc(Long menuId, String deltYn, String rgstUserId);
    // 작성자(rgstUserId) 기준 - 전체 검색
    List<MvcBbs> findByDeltYnAndRgstUserIdOrderByRgstDayDesc(String deltYn, String rgstUserId);

    @Query("SELECT m FROM MvcBbs m WHERE m.deltYn = :deltYn AND m.menuId = :menuId ORDER BY m.rgstDay DESC")
    Page<MvcBbs> findByMenuIdAndDeltYnOrderByRgstDayDesc(@Param("menuId") long menuId, @Param("deltYn") String deltYn, Pageable pageable);

    @Query(
            "SELECT b.pstId AS pstId, " +
                    "       b.ttl AS ttl, " +
                    "       b.nickNm AS nickNm, " +
                    "       b.rgstDay AS rgstDay, " +
                    "       COALESCE(rc.count, 0) AS rcmdtnCount, " +
                    "       COALESCE(cmt.commentCount, 0) AS commentCount " +
                    "FROM MvcBbs b " +
                    "LEFT JOIN ( " +
                    "    SELECT r.rcmdtnSeId AS pstId, " +
                    "           r.menuId AS menuId, " +
                    "           COUNT(r) AS count " +
                    "    FROM MvcRcmdtnInfo r " +
                    "    WHERE r.deltYn = 'N' " +
                    "    GROUP BY r.rcmdtnSeId, r.menuId " +
                    ") rc ON b.pstId = rc.pstId AND b.menuId = rc.menuId " +
                    "LEFT JOIN ( " +
                    "    SELECT c.pstId AS pstId, " +
                    "           COUNT(c) AS commentCount " +
                    "    FROM MvcBbsCmnt c " +
                    "    WHERE c.deltYn = 'N' " +
                    "    GROUP BY c.pstId " +
                    ") cmt ON b.pstId = cmt.pstId " +
                    "WHERE b.deltYn = 'N' " +
                    "AND b.menuId = :menuId " +
                    "ORDER BY b.rgstDay DESC"
    )
    Page<Object[]> findWithCountsByMenuIdAndDeltYn(@Param("menuId") long menuId, Pageable pageable);
    // 메뉴 ID와 페이징정보로 받은다음, 목록에 보여줄 정보(게시물번호,제목,닉네임,등록일,추천합,댓글합) 리스트로 반환

}
