package com.personal.case_study_promotions.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Utility {

    public static String convertToUTCDate(String date) {
        String originalTime = date.substring(0, 25);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(originalTime, formatter);

        LocalDateTime utcTime = offsetDateTime.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
        return utcTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
