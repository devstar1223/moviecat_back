package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcMbrInfo;
import com.moviecat.www.entity.MvcScrBbs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MvcScrBbsRepository extends JpaRepository<MvcScrBbs, Long> {
    Optional<MvcScrBbs> findByScrIdAndDeltYn(long scrId,String deltYn);
    List<MvcScrBbs> findByMenuIdAndDeltYnOrderByScrIdDesc(long menuId, String deltYn);
    List<MvcScrBbs> findByDeltYnOrderByScrIdDesc(String deltYn);
    List<MvcScrBbs> findByVdoNmContainingOrderByRgstDayDesc(String vdoNm);
    List<MvcScrBbs> findByRgstUserIdAndDeltYnOrderByRgstDayDesc(String rgstUserId, String deltYn);

    List<MvcScrBbs> findByDeltYnAndVdoNmContainingOrVdoNmEnContainingOrderByRgstDayDesc(String deltYn, String vdoNm, String vdoNmEn);
}
