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
public class NewBudegetRequest {
    private String customerName;
    private String mainDriverDocument;
    @PastOrPresent
    private LocalDate mainDriverBirthDate;
    private Long carId;
    private List<DriverDTO> drivers;
}
