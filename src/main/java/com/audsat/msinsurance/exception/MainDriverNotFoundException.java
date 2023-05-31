package com.audsat.msinsurance.exception;

public class MainDriverNotFoundException extends RuntimeException {
    public MainDriverNotFoundException(String document) {
        super(String.format("Main driver not found with document: %s", document));
    }
}
