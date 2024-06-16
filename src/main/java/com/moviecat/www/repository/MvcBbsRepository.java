package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcBbs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MvcBbsRepository extends JpaRepository<MvcBbs, Long> {
    List<MvcBbs> findByMenuIdAndDeltYnOrderByPstIdAsc(Long menuId, String deltYn);
    Optional<MvcBbs> findByMenuIdAndPstIdAndDeltYn(long menuId, long pstId, String deltYn);

    Optional<MvcBbs> findByPstIdAndDeltYn(long pstId, String deltYn);
}
