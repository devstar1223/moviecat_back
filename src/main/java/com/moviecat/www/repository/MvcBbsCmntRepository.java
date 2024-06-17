package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcBbsCmnt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MvcBbsCmntRepository extends JpaRepository<MvcBbsCmnt,Long> {
    Optional<MvcBbsCmnt> findTopByPstIdOrderBySeqDesc(long pstId);
    Optional<MvcBbsCmnt> findTopByPstIdOrderByCmntGroupDesc(long pstId);
}
