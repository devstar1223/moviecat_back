package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcBbsCmnt;
import jakarta.validation.constraints.Future;
import org.springframework.data.jpa.repository.JpaRepository;
import org.thymeleaf.spring6.expression.Mvc;

import java.util.List;
import java.util.Optional;

public interface MvcBbsCmntRepository extends JpaRepository<MvcBbsCmnt,Long> {
    Optional<MvcBbsCmnt> findTopByPstIdAndCmntGroupOrderBySeqDesc(long pstId, int cmntGroup);
    Optional<MvcBbsCmnt> findTopByPstIdOrderByCmntGroupDesc(long pstId);

    Optional<MvcBbsCmnt> findByCmntIdAndDeltYn(long cmntId, char deltYn);
    List<MvcBbsCmnt> findByPstIdAndSeqGreaterThanEqual(long pstId, int seq);

    List<MvcBbsCmnt> findByPstIdAndDeltYnOrderByCmntGroupAscSeqAsc(long pstId, char deltYn);

    List<MvcBbsCmnt> findByRgstUserIdAndDeltYnOrderByRgstDayDesc(String rgstUserId, char deltYn);
    Optional<MvcBbsCmnt> findTopByUpCmntIdOrderBySeqDesc(long upCmntId);

    List<MvcBbsCmnt> findByPstIdAndDeltYn(long pstId, char deltYn);

    Optional<MvcBbsCmnt> findByPstId(long pstId);

}
