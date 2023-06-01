package com.audsat.msinsurance.exception;

public class InsuranceNotFoundException extends RuntimeException {
    public InsuranceNotFoundException(Long id) {
        super(String.format("Não foi encontrado nenhum orçamento de seguro para esse id %s", id));
    }
}
