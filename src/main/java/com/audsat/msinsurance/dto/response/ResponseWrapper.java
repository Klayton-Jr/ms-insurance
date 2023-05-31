package com.audsat.msinsurance.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseWrapper {
    private String message;
    private Integer code;
    private Object insurance;
}
