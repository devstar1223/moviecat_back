package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MvcMenuRepository extends JpaRepository<MvcMenu, Long> {
    List<MvcMenu> findByUseYnOrderBySeqAsc(String useYn); //동적 쿼리 메서드
}
