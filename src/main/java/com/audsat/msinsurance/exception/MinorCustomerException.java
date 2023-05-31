package com.audsat.msinsurance.exception;

public class MinorCustomerException extends RuntimeException {

    public MinorCustomerException(String customer) {
        super(String.format("Driver, %s, is minor and cannot be a driver", customer));
    }
}
