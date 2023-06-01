package com.audsat.msinsurance.dto.request;

import com.audsat.msinsurance.dto.DriverDTO;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateBudgetRequest {
    private Long customerId;
    private String mainDriverDocument;
    @PastOrPresent(message = "Data de nascimento do motorista principal não pode ser maior que a atual")
    private LocalDate mainDriverBirthDate;
    private Long carId;
    private List<DriverDTO> drivers;
    private boolean activate;
}
