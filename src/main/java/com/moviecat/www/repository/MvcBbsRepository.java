package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcBbs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MvcBbsRepository extends JpaRepository<MvcBbs, Long> {
    List<MvcBbs> findByMenuIdAndDeltYnOrderByPstIdAsc(Long menuId, String deltYn);
}
