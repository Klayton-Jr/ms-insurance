package com.audsat.msinsurance.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super(String.format("Cutomer not found by id: %s", id));
    }
}
