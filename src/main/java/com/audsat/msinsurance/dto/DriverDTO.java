package com.audsat.msinsurance.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DriverDTO {
    private String driverName;
    private String driverDocument;
    private LocalDate driverBirthDate;
}
