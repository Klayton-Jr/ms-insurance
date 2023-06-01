package com.audsat.msinsurance.dto;

import com.audsat.msinsurance.model.Drivers;
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

    public static DriverDTO of(Drivers drivers) {
        return DriverDTO.builder()
                .driverName(drivers.getCustomer().getName())
                .driverDocument(drivers.getDocument())
                .driverBirthDate(drivers.getBirthdate())
                .build();
    }
}
