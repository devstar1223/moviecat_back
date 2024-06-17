package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcBbsCmnt;
import jakarta.validation.constraints.Future;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MvcBbsCmntRepository extends JpaRepository<MvcBbsCmnt,Long> {
    Optional<MvcBbsCmnt> findTopByPstIdOrderBySeqDesc(long pstId);
    Optional<MvcBbsCmnt> findTopByPstIdOrderByCmntGroupDesc(long pstId);

    Optional<MvcBbsCmnt> findByCmntIdAndDeltYn(long cmntId, char deltYn);
    List<MvcBbsCmnt> findByPstIdAndSeqGreaterThanEqual(long pstId, int seq);

    List<MvcBbsCmnt> findByPstIdAndDeltYnOrderBySeqAsc(long pstId, char deltYn);
}
