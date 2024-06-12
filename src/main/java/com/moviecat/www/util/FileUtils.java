package com.moviecat.www.util;

public class FileUtils {

    public static String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex > 0) {
            return fileName.substring(lastIndex + 1);
        } else {
            return ""; // 확장자가 없는 경우
        }
    }
}