package com.moviecat.www.repository;

import com.moviecat.www.entity.MvcAtchFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MvcAtchFileRepository extends JpaRepository<MvcAtchFile, Long> {
    Optional<MvcAtchFile> findTopByOrderByAtchFileIdCkAtchFileIdDesc();
}
