package com.moviecat.www.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationUtil {
    public <T> List<T> getPageLimit(List<T> list, int page, int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("페이지 사이즈는 양수이어야 합니다");
        }
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, list.size());
        if (start >= list.size()) {
            throw new IndexOutOfBoundsException("페이지 범위 초과");
        }
        if (page <= 0) {
            throw new IllegalArgumentException("페이지 값으로 0이 오는 것은 유효하지 않음");
        }
        return list.subList(start, end);
    }
}
