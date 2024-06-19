package com.moviecat.www.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TimeFormat {
    public String formatDate(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return sdf.format(timestamp);
    }

    public String[] formatDateToday(Timestamp timestamp) {
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

        String today = sdfDay.format(Timestamp.valueOf(LocalDateTime.now()));
        String rgstDay = sdfDay.format(timestamp);

        if (today.equals(rgstDay)) {
            return new String[]{sdfTime.format(timestamp), "Y"};
        } else {
            return new String[]{sdfDay.format(timestamp), "N"};
        }
    }
}
