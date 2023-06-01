package com.audsat.msinsurance.advice;

import com.audsat.msinsurance.dto.response.ResponseWrapper;
import com.audsat.msinsurance.exception.CarNotFoundException;
import com.audsat.msinsurance.exception.InsuranceNotFoundException;
import com.audsat.msinsurance.exception.MainDriverNotFoundException;
import com.audsat.msinsurance.exception.MinorCustomerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class InsuranceAdvice {

    @ExceptionHandler(CarNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseWrapper handleCarNotFoundException(CarNotFoundException carNotFoundException, WebRequest request) {
        return buildGenericErrorResponse(carNotFoundException);
    }

    @ExceptionHandler(MinorCustomerException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseWrapper handleMinorCustomerException(MinorCustomerException minorCustomerException, WebRequest request) {
        return buildGenericErrorResponse(minorCustomerException);
    }

    @ExceptionHandler(MainDriverNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseWrapper handleMainDriverNotFoundException(MainDriverNotFoundException mainDriverNotFoundException, WebRequest request) {
        return buildGenericErrorResponse(mainDriverNotFoundException);
    }

    @ExceptionHandler(InsuranceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseWrapper handleInsuranceNotFoundException(InsuranceNotFoundException insuranceNotFoundException, WebRequest request) {
        return buildGenericErrorResponse(insuranceNotFoundException);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> getErrorsMap(List<String> errors) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorMessages", errors);
        errorResponse.put("code", HttpStatus.BAD_REQUEST.value());
        return errorResponse;
    }

    private ResponseWrapper buildGenericErrorResponse(Exception exception) {
        return ResponseWrapper.builder()
                        .message(exception.getMessage())
                        .code(HttpStatus.BAD_REQUEST.value())
                        .build();
    }

}
