package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcBbs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface MvcBbsRepository extends JpaRepository<MvcBbs, Long> {
    List<MvcBbs> findByMenuIdAndDeltYnOrderByPstIdDesc(Long menuId, String deltYn);
    Optional<MvcBbs> findByMenuIdAndPstIdAndDeltYn(long menuId, long pstId, String deltYn);
    Optional<MvcBbs> findByPstIdAndDeltYn(long pstId, String deltYn);

    List<MvcBbs> findByRgstUserIdAndDeltYnOrderByRgstDayDesc(String rgstUserId, String deltYn);

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
}
