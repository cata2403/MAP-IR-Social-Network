package com.ubb.infrastructure_layer.utils;

import java.time.format.DateTimeFormatter;

public final class DateTimeFormats {

    private static final DateTimeFormatter dateFormatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter dateFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Returns a datetime format of pattern dd/mm/yyyy
     * @return format for datetime
     */
    public static DateTimeFormatter getDateFormatter1(){
        return dateFormatter1;
    }

    /**
     * Returns a datetime format of pattern yyyy-mm-dd
     * @return format for datetime
     */
    public static DateTimeFormatter getDateFormatter2(){
        return dateFormatter2;
    }
}
