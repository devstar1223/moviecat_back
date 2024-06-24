package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcAtchFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MvcAtchFileRepository extends JpaRepository<MvcAtchFile, Long> {
    Optional<MvcAtchFile> findTopByOrderByAtchFileIdCkAtchFileIdDesc();
    Optional<MvcAtchFile> findTopByAtchFileIdCkAtchFileIdOrderByAtchFileIdCkSeqDesc(long atchFileId);
    List<MvcAtchFile> findAllByAtchFileIdCkAtchFileIdAndDeltYnOrderByAtchFileIdCkSeqAsc(long fileId,String deltYn);

    List<MvcAtchFile> findAllByAtchFileIdCkAtchFileIdAndAtchFileIdCkSeq(long atchFileId, int seq);
}
