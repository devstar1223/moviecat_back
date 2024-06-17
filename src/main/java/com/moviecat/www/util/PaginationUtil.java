package com.moviecat.www.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationUtil {
    public static final int PAGE_SIZE = 10; // 페이지 크기 고정값
    public static <T> List<T> getPage(List<T> list, int page) {
        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, list.size());
        if (start > list.size()) {
            throw new IndexOutOfBoundsException("페이지 범위 초과");
        }
        if (page == 0) {
            throw new IllegalArgumentException("페이지 값으로 0이 오는것은 유효하지 않음");
        }
        return list.subList(start, end);
    }
}