package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcScrBbs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MvcScrBbsRepository extends JpaRepository<MvcScrBbs, Long> {
    Optional<MvcScrBbs> findByScrIdAndDeltYn(long scrId,String deltYn);
}
