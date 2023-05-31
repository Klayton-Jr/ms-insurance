package com.audsat.msinsurance.service.impl;

import com.audsat.msinsurance.dto.request.NewBudegetRequest;
import com.audsat.msinsurance.exception.CarNotFoundException;
import com.audsat.msinsurance.repository.CarsRepository;
import com.audsat.msinsurance.service.InsuranceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InsuranceServiceImplTest {

    @Autowired
    InsuranceService insuranceService;

    @MockBean
    CarsRepository carsRepository;

    @Test
    @DisplayName("Return CarNotFoundException if carId didn't return any record")
    void createInsurance() {
        NewBudegetRequest budegetRequest = NewBudegetRequest.builder()
                .carId(0L)
                .build();
        when(carsRepository.findById(budegetRequest.getCarId())).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> insuranceService.createInsurance(budegetRequest));
    }

    @Test
    void updateInsurance() {
    }

    @Test
    void deleteInsurance() {
    }
}