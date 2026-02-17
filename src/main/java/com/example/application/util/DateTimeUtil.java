package com.example.application.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public final class DateTimeUtil {

    private DateTimeUtil() {
    }

    public static String formatLong(Instant instant) {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).format(toZonedDateTime(instant));
    }

    public static String formatShort(Instant instant) {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(toZonedDateTime(instant));
    }

    private static ZonedDateTime toZonedDateTime(Instant instant) {
        return instant.atZone(ZoneId.systemDefault());
    }
}
