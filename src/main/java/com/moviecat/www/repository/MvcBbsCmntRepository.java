package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcBbsCmnt;
import jakarta.validation.constraints.Future;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.thymeleaf.spring6.expression.Mvc;

import java.util.List;
import java.util.Optional;

public interface MvcBbsCmntRepository extends JpaRepository<MvcBbsCmnt,Long> {
    Optional<MvcBbsCmnt> findTopByPstIdAndCmntGroupOrderBySeqDesc(long pstId, int cmntGroup);
    Optional<MvcBbsCmnt> findTopByPstIdOrderByCmntGroupDesc(long pstId);

    Optional<MvcBbsCmnt> findByCmntIdAndDeltYn(long cmntId, char deltYn);
    List<MvcBbsCmnt> findByPstIdAndSeqGreaterThanEqual(long pstId, int seq);

    List<MvcBbsCmnt> findByPstIdOrderByCmntGroupAscSeqAsc(long pstId);
    List<MvcBbsCmnt> findByPstIdAndDeltYnAndCmntLyrOrderByCmntGroupAscSeqAsc(long pstId, char deltYn, int lyr);

    Optional<MvcBbsCmnt> findByCmntGroupAndDeltYn(long cmntGroup, char deltYn);

    List<MvcBbsCmnt> findByRgstUserIdAndDeltYnOrderByRgstDayDesc(String rgstUserId, char deltYn);
    Optional<MvcBbsCmnt> findTopByUpCmntIdOrderBySeqDesc(long upCmntId);

    List<MvcBbsCmnt> findByPstIdAndDeltYn(long pstId, char deltYn);

    Optional<MvcBbsCmnt> findByPstId(long pstId);

    @Query("SELECT COUNT(c) FROM MvcBbsCmnt c WHERE c.pstId = :pstId AND c.deltYn = 'N'")
    int countByPstIdAndDeltYn(@Param("pstId") long pstId);

}
