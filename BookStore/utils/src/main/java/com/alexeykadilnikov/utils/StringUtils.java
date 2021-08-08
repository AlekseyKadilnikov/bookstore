package com.alexeykadilnikov.utils;

public class StringUtils {
    public static boolean isNumeric(String str) {
        return str.matches("\\d+");
    }
    public static boolean isDate(String str) {
        return str.matches("^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$");
    }
}
