package com.audsat.msinsurance.dto;

import com.audsat.msinsurance.exception.MainDriverNotFoundException;
import com.audsat.msinsurance.model.CarDrivers;
import com.audsat.msinsurance.model.Insurances;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BudgetDTO {
    private String customername;
    private String mainDriverDocument;
    private CarDTO car;
    private List<DriverDTO> otherDrivers;
    private BigDecimal value;
    private Boolean activated;
    private Long insuranceId;

    public static BudgetDTO of(Insurances insurance) {
        return BudgetDTO.builder()
                .car(CarDTO.of(insurance.getCars()))
                .customername(insurance.getCustomer().getName())
                .mainDriverDocument(
                        insurance.getCars().getCarDriversList().stream()
                                .filter(CarDrivers::getMainDriver)
                                .findFirst().orElseThrow(() -> new MainDriverNotFoundException("")).getDrivers().getDocument()
                )
                .otherDrivers(
                        insurance.getCars().getCarDriversList().stream()
                                .filter(carDrivers -> !carDrivers.getMainDriver())
                                .map(carDrivers -> DriverDTO.of(carDrivers.getDrivers()))
                                .toList()
                )
                .value(insurance.getAmount())
                .activated(insurance.getActive())
                .insuranceId(insurance.getId())
                .build();
    }
}
