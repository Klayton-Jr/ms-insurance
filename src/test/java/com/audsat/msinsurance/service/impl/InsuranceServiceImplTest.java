package com.audsat.msinsurance.service.impl;

import com.audsat.msinsurance.dto.DriverDTO;
import com.audsat.msinsurance.dto.request.NewBudegetRequest;
import com.audsat.msinsurance.exception.CarNotFoundException;
import com.audsat.msinsurance.exception.MinorCustomerException;
import com.audsat.msinsurance.model.Cars;
import com.audsat.msinsurance.repository.CarsRepository;
import com.audsat.msinsurance.service.InsuranceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
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
    void createInsuranceWithNoCar() {
        NewBudegetRequest budegetRequest = NewBudegetRequest.builder()
                .carId(0L)
                .customerName("Custom customer name")
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1999, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(1996, 1, 1))
                        .driverName("other driver name")
                        .build()))
                .build();
        when(carsRepository.findById(budegetRequest.getCarId())).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> insuranceService.createInsurance(budegetRequest));
    }

    @Test
    @DisplayName("Return MinorCustomerException if main driver is minor")
    void createInsuranceWithMainMinorDriver() {
        NewBudegetRequest budegetRequest = NewBudegetRequest.builder()
                .carId(1L)
                .customerName("Custom customer name")
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(2020, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                                .driverBirthDate(LocalDate.of(1996, 1, 1))
                                .driverName("other driver name")
                        .build()))
                .build();
        when(carsRepository.findById(budegetRequest.getCarId())).thenReturn(Optional.of(new Cars()));
        assertThrows(MinorCustomerException.class, () -> insuranceService.createInsurance(budegetRequest));
    }

    @Test
    @DisplayName("Return MinorCustomerException if another driver is minor")
    void createInsuranceWithAnotherMinorDriver() {
        NewBudegetRequest budegetRequest = NewBudegetRequest.builder()
                .carId(1L)
                .customerName("Custom customer name")
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1999, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(2020, 1, 1))
                        .driverName("other driver name")
                        .build()))
                .build();
        when(carsRepository.findById(budegetRequest.getCarId())).thenReturn(Optional.of(new Cars()));
        assertThrows(MinorCustomerException.class, () -> insuranceService.createInsurance(budegetRequest));
    }

    @Test
    void updateInsurance() {
    }

    @Test
    void deleteInsurance() {
    }
}