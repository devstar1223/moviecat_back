package com.moviecat.www.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileUtils {

    public String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex > 0) {
            return fileName.substring(lastIndex + 1);
        } else {
            return ""; // 확장자가 없는 경우
        }
    }
    public String removeFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex > 0) {
            return fileName.substring(0, lastIndex);
        } else {
            return fileName; // 확장자가 없는 경우 원본 파일명 반환
        }
    }
}