package com.ubb.utils;

import java.time.format.DateTimeFormatter;

public final class DateTimeFormats {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static DateTimeFormatter getDateFormatter(){
        return dateFormatter;
    }
}
