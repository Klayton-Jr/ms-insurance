package com.audsat.msinsurance.exception;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(Long id) {
        super(String.format("Car not found by id %s", id));
    }
}
