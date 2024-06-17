package com.moviecat.www.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Component
@RequiredArgsConstructor
public class TimeFormat {
    public String formatDate(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return sdf.format(timestamp);
    }
}
