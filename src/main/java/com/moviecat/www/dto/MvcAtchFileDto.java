package com.moviecat.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
public class MvcAtchFileDto {
        private long atchFileId; // 파일 id
        private long seq; // 순서
        private long pstId; // 게시글 id
        private MultipartFile multipartFile; // 사진 원본 파일
        private String actlFileNm; // 실제파일명
        private String strgFileNm; // 저장파일명
        private String strgFilePath; // 저장파일경로
        private int strgFileSize; // 저장파일사이즈
        private String strgFileExtn; // 저장파일확장자
        private String rgstUserId; // 등록ID
        private String rgstUserNm; // 등록자
        private Timestamp rgstDay; // 등록일
        private String mdfcnUserId; // 수정ID
        private String mdfcnUserNm; // 수정자
        private Timestamp mdfcnDay; // 수정일
        private String deltYn = "N"; // 삭제유무, 기본 "N"
}
