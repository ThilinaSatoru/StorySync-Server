package com.satoru.pdfadmin.utils;

import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StringToTimestampConverter implements Converter<String, Timestamp> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Timestamp convert(String source) {
        try {
            return new Timestamp(dateFormat.parse(source).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd HH:mm:ss", e);
        }
    }
}
