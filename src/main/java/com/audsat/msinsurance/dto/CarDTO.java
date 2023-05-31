package com.audsat.msinsurance.dto;

import com.audsat.msinsurance.model.Cars;
import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarDTO {
    private String model;
    private String year;
    private String manufacture;
    private BigDecimal fipeValue;

    public static CarDTO of(Cars cars) {
        return CarDTO.builder()
                .model(cars.getModel())
                .year(cars.getYear())
                .manufacture(cars.getManufacturer())
                .fipeValue(cars.getFipeValue())
                .build();
    }
}
