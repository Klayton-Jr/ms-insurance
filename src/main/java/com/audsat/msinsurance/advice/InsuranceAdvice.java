package com.audsat.msinsurance.advice;

import com.audsat.msinsurance.dto.response.ResponseWrapper;
import com.audsat.msinsurance.exception.CarNotFoundException;
import com.audsat.msinsurance.exception.MinorCustomerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class InsuranceAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CarNotFoundException.class)
    ResponseEntity<ResponseWrapper> handleCarNotFoundException(CarNotFoundException carNotFoundException, WebRequest request) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .message(carNotFoundException.getMessage())
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MinorCustomerException.class)
    ResponseEntity<ResponseWrapper> handleMinorCustomerException(MinorCustomerException minorCustomerException, WebRequest request) {
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .message(minorCustomerException.getMessage())
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

}
