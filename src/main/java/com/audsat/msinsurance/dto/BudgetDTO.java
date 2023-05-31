package com.audsat.msinsurance.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BudgetDTO {
    private String mainDriverName;
    private String mainDriverDocument;
    private CarDTO car;
    private List<DriverDTO> otherDrivers;
    private BigDecimal value;
}
