package com.moviecat.www.util;

public class FormatUtils {

    /**
     * 폰 번호 - 형식으로 변경
     * @param phoneNumber
     * @return
     */
    public static String formatPhoneNumber(String phoneNumber) {
        // 숫자만 추출
        String digits = phoneNumber.replaceAll("\\D", "");

        // 형식 적용
        if (digits.length() >= 11) {
            return digits.substring(0, 3) + "-" + digits.substring(3, 7) + "-" + digits.substring(7, 11);
        } else if (digits.length() >= 7) {
            return digits.substring(0, 3) + "-" + digits.substring(3, 7) + "-" + digits.substring(7);
        } else if (digits.length() >= 3) {
            return digits.substring(0, 3) + "-" + digits.substring(3);
        } else {
            return digits; // 기본적으로 숫자가 적으면 그대로 반환
        }
    }
}
