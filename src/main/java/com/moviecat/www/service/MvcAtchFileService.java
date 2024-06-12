package com.moviecat.www.service;

import com.moviecat.www.dto.MvcAtchFileDto;
import com.moviecat.www.entity.MvcAtchFile;
import com.moviecat.www.repository.MvcAtchFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MvcAtchFileService {
    private final MvcAtchFileRepository mvcAtchFileRepository;

    public void uploadAtchFile(MvcAtchFileDto mvcAtchFileDto){
        // TODO. 현재 dto에 전부 설정한 후, db에는 dto의 정보대로 또다시 등록하는 비효율적인 방식 / 수정예정.
        MvcAtchFile newFile = new MvcAtchFile();
        newFile.setAtchFileId(mvcAtchFileDto.getAtchFileId());
        newFile.setPstId(mvcAtchFileDto.getPstId());
        newFile.setSeq(mvcAtchFileDto.getSeq());
        newFile.setActlFileNm(mvcAtchFileDto.getActlFileNm());
        newFile.setStrgFileNm(mvcAtchFileDto.getStrgFileNm());
        newFile.setStrgFilePath(mvcAtchFileDto.getStrgFilePath()); // 아까 path는 uploadService로 설정했음
        newFile.setStrgFileSize(mvcAtchFileDto.getStrgFileSize());
        newFile.setStrgFileExtn(mvcAtchFileDto.getStrgFileExtn());
        newFile.setRgstUserId(mvcAtchFileDto.getRgstUserId());
        newFile.setRgstUserNm(mvcAtchFileDto.getRgstUserNm());
        newFile.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
        newFile.setMdfcnUserId(mvcAtchFileDto.getMdfcnUserId());
        newFile.setMdfcnUserNm(mvcAtchFileDto.getMdfcnUserNm());
        newFile.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
        newFile.setDeltYn(mvcAtchFileDto.getDeltYn());
        mvcAtchFileRepository.save(newFile);
    }


}
