package com.moviecat.www.service;

import com.moviecat.www.dto.MvcAtchFileDto;
import com.moviecat.www.dto.MvcBbsDto;
import com.moviecat.www.entity.MvcAtchFile;
import com.moviecat.www.entity.MvcAtchFilePK;
import com.moviecat.www.repository.MvcAtchFileRepository;
import com.moviecat.www.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring6.expression.Mvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MvcAtchFileService {

    private final MvcAtchFileRepository mvcAtchFileRepository;
    private final MvcFileUploadService mvcFileUploadService;

    public MvcAtchFileDto writeSetDto(MultipartFile multipartFile, MvcBbsDto mvcBbsDto, int i){
        Optional<MvcAtchFile> fileIdOptional = mvcAtchFileRepository.findTopByOrderByAtchFileIdCkAtchFileIdDesc(); // 복합키 id에서 가장 큰 값을 가져온다
        long fileId; // 먼저 fileId 선언
        if(fileIdOptional.isPresent() && i == 1){
            fileId = fileIdOptional.get().getAtchFileIdCk().getAtchFileId() + 1; // 등록된 파일이 하나라도 있고, 리스트중 첫번째 파일이면, 최신번호+1 부여
        }
        else if(fileIdOptional.isPresent() && i > 1) {
            fileId = fileIdOptional.get().getAtchFileIdCk().getAtchFileId(); // 등록된 파일이 있고, 리스트중 두번째 파일이면, 최신번호 부여
        }
        else{
            fileId = 1; // 등록된 파일이 하나도 없다면 1번 부여
        }
        MvcAtchFileDto newFileDto = new MvcAtchFileDto(); // dto를 만들어서 값을 넣자
        newFileDto.setAtchFileId(fileId);
        newFileDto.setSeq(i);
        newFileDto.setMultipartFile(multipartFile);
        newFileDto.setActlFileNm(multipartFile.getOriginalFilename());
        newFileDto.setStrgFileNm("임시 이름"); //TODO. S3 저장 파일명 규칙 생성 예정
        newFileDto.setStrgFilePath(mvcFileUploadService.uploadFile(multipartFile)); // 파일업로드 서비스에서 등록하고 주소명 반환
        newFileDto.setStrgFileSize((int) multipartFile.getSize());
        newFileDto.setStrgFileExtn(FileUtils.getFileExtension(multipartFile.getOriginalFilename())); // 파일 확장자 추출은 util에서
        newFileDto.setRgstUserId(mvcBbsDto.getMbrId()); // 글 등록자 ID와 같음
        newFileDto.setRgstUserNm(mvcBbsDto.getMbrNm()); // 글 등록자 이름과 같음
        newFileDto.setRgstDay(Timestamp.valueOf(LocalDateTime.now())); // 현재시간
        newFileDto.setMdfcnUserId(mvcBbsDto.getMbrId()); // 글 등록자 ID와 같음
        newFileDto.setMdfcnUserNm(mvcBbsDto.getMbrNm()); // 글 등록자 이름과 같음
        newFileDto.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 현재시간
        newFileDto.setDeltYn("N"); // 등록시엔 삭제유무 기본 N
        return newFileDto;
    }

    public void uploadAtchFile(MvcAtchFileDto mvcAtchFileDto){
        System.out.println(mvcAtchFileDto.toString());
        MvcAtchFile newFile = new MvcAtchFile();

        MvcAtchFilePK atchFileIdCk = new MvcAtchFilePK(); // 복합키에 값 set 하기위해 클래스 만들기
        atchFileIdCk.setAtchFileId(mvcAtchFileDto.getAtchFileId()); // id 설정
        atchFileIdCk.setSeq(mvcAtchFileDto.getSeq()); // seq 설정
        newFile.setAtchFileIdCk(atchFileIdCk); // 복합키에 반영

        newFile.setActlFileNm(mvcAtchFileDto.getActlFileNm());
        newFile.setStrgFileNm(mvcAtchFileDto.getStrgFileNm());
        newFile.setStrgFilePath(mvcAtchFileDto.getStrgFilePath()); // 아까 path는 uploadService로 설정했음
        newFile.setStrgFileSize(mvcAtchFileDto.getStrgFileSize());
        newFile.setStrgFileExtn(mvcAtchFileDto.getStrgFileExtn());
        newFile.setRgstUserId(mvcAtchFileDto.getRgstUserId());
        newFile.setRgstUserNm(mvcAtchFileDto.getRgstUserNm());
        newFile.setRgstDay(Timestamp.valueOf(LocalDateTime.now()));
        newFile.setMdfcnUserId(mvcAtchFileDto.getMdfcnUserId()); // 수정자 ID 설정
        newFile.setMdfcnUserNm(mvcAtchFileDto.getMdfcnUserNm()); // 수정자 이름 설정
        newFile.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now()));
        newFile.setDeltYn(mvcAtchFileDto.getDeltYn());
        mvcAtchFileRepository.save(newFile);
    }

    public MvcAtchFileDto editSetDto(MultipartFile multipartFile, MvcBbsDto mvcBbsDto, Long atchFileId){
        MvcAtchFileDto newFileDto = new MvcAtchFileDto(); // dto를 만들어서 값을 넣자
        long recentSeq;
        if(atchFileId == null){
            Optional<MvcAtchFile> fileIdOptional = mvcAtchFileRepository.findTopByOrderByAtchFileIdCkAtchFileIdDesc(); // 복합키 id에서 가장 큰 값을 가져온다.
            if(fileIdOptional.isPresent()) {
                atchFileId = fileIdOptional.get().getAtchFileIdCk().getAtchFileId()+1; // 등록된 파일이 있으면 최신번호+1 부여
            }
            else{
                atchFileId = (long)1; // 등록된 파일이 하나도 없다면 1번 부여
            }
            recentSeq = 0;
        }
        else{
            Optional<MvcAtchFile> recentSeqOptional = mvcAtchFileRepository.findTopByAtchFileIdCkAtchFileIdOrderByAtchFileIdCkSeqDesc(atchFileId); // id와 일치하는 가장 큰 seq 갑을 가져온다.
            recentSeq = recentSeqOptional.get().getAtchFileIdCk().getSeq();
        }
        newFileDto.setAtchFileId(atchFileId);
        newFileDto.setSeq(recentSeq+1); // 순서는 +1 해줌
        newFileDto.setMultipartFile(multipartFile);
        newFileDto.setActlFileNm(multipartFile.getOriginalFilename());
        newFileDto.setStrgFileNm("임시 이름"); //TODO. S3 저장 파일명 규칙 생성 예정
        newFileDto.setStrgFilePath(mvcFileUploadService.uploadFile(multipartFile)); // 파일업로드 서비스에서 등록하고 주소명 반환
        newFileDto.setStrgFileSize((int) multipartFile.getSize());
        newFileDto.setStrgFileExtn(FileUtils.getFileExtension(multipartFile.getOriginalFilename())); // 파일 확장자 추출은 util에서
        newFileDto.setRgstUserId(mvcBbsDto.getRgstUserId()); // 글 등록자 ID와 같음
        newFileDto.setRgstUserNm(mvcBbsDto.getRgstUserNm()); // 글 등록자 이름과 같음
        newFileDto.setRgstDay(Timestamp.valueOf(LocalDateTime.now())); // 현재시간
        newFileDto.setMdfcnUserId(mvcBbsDto.getRgstUserId()); // 글 등록자 ID와 같음
        newFileDto.setMdfcnUserNm(mvcBbsDto.getRgstUserNm()); // 글 등록자 이름과 같음
        newFileDto.setMdfcnDay(Timestamp.valueOf(LocalDateTime.now())); // 현재시간
        newFileDto.setDeltYn("N"); // 등록시엔 삭제유무 기본 N
        return newFileDto;
    }

}
