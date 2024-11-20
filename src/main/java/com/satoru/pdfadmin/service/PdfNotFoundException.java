package com.satoru.pdfadmin.service;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND)
public class PdfNotFoundException extends Exception {
    public PdfNotFoundException(String message) {
        super(message);
    }
}
