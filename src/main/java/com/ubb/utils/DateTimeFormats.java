package com.ubb.utils;

import java.time.format.DateTimeFormatter;

public final class DateTimeFormats {

    private static final DateTimeFormatter dateFormatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter dateFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static DateTimeFormatter getDateFormatter1(){
        return dateFormatter1;
    }

    public static DateTimeFormatter getDateFormatter2(){
        return dateFormatter2;
    }
}
