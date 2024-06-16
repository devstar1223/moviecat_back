package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcBbs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface MvcBbsRepository extends JpaRepository<MvcBbs, Long> {
    List<MvcBbs> findByMenuIdAndDeltYnOrderByPstIdAsc(Long menuId, String deltYn);
    Optional<MvcBbs> findByMenuIdAndPstIdAndDeltYn(long menuId, long pstId, String deltYn);
    Optional<MvcBbs> findByPstIdAndDeltYn(long pstId, String deltYn);

    // 0. 제목(ttl) 기준
    List<MvcBbs> findByMenuIdAndDeltYnAndTtlContainingOrderByRgstDayDesc(Long menuId, String deltYn, String ttl);

//    // 1. 제목(ttl) 또는 내용(cn) 기준
//    List<MvcBbs> findByMenuIdAndDeltYnAndTtlContainingOrCnContainingOrderByRgstDayDesc(Long menuId,String deltYn,String srchWord);
//
//    // 2. 작성자(rgstUserId) 기준
//    List<MvcBbs> findByMenuIdAndDeltYnAndRgstUserIdOrderByRgstDayDesc(Long menuId, String deltYn, String rgstUserId);
}
