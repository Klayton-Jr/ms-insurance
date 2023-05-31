package com.audsat.msinsurance.dto.request;

import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewBudegetRequest {
    private String customerName;
    private String driverDocument;
    private LocalDate driverBirthDate;
    private Long carId;
}
