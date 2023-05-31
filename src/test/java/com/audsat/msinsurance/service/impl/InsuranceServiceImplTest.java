package com.audsat.msinsurance.service.impl;

import com.audsat.msinsurance.dto.DriverDTO;
import com.audsat.msinsurance.dto.request.NewBudgetRequest;
import com.audsat.msinsurance.exception.CarNotFoundException;
import com.audsat.msinsurance.exception.MinorCustomerException;
import com.audsat.msinsurance.model.Cars;
import com.audsat.msinsurance.repository.CarsRepository;
import com.audsat.msinsurance.repository.ClaimsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsuranceServiceImplTest {

    @InjectMocks
    InsuranceServiceImpl insuranceService;
    @Mock
    CarsRepository carsRepository;
    @Mock
    ClaimsRepository claimsRepository;


    @Test
    @DisplayName("Return CarNotFoundException if carId didn't return any record")
    void createInsuranceWithNoCar() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(0L)
                .customerName("Custom customer name")
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1999, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(1996, 1, 1))
                        .driverName("other driver name")
                        .build()))
                .build();
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> insuranceService.createInsurance(budgetRequest));
    }

    @Test
    @DisplayName("Return MinorCustomerException if main driver is minor")
    void createInsuranceWithMainMinorDriver() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerName("Custom customer name")
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(2020, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                                .driverBirthDate(LocalDate.of(1996, 1, 1))
                                .driverName("other driver name")
                        .build()))
                .build();
        assertThrows(MinorCustomerException.class, () -> insuranceService.createInsurance(budgetRequest));
    }

    @Test
    @DisplayName("Return MinorCustomerException if another driver is minor")
    void createInsuranceWithAnotherMinorDriver() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerName("Custom customer name")
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1999, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(2020, 1, 1))
                        .driverName("other driver name")
                        .build()))
                .build();
        assertThrows(MinorCustomerException.class, () -> insuranceService.createInsurance(budgetRequest));
    }

    @Test
    @DisplayName("Return minimum budget percentage")
    void createInsuranceWithNoRiskMainDriver() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerName("Custom customer name")
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1980, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(1996, 1, 1))
                        .driverName("other driver name")
                        .build()))
                .build();
        BigDecimal fipeValueCar = new BigDecimal(40000);
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.of(Cars.builder().fipeValue(fipeValueCar).build()));
        BigDecimal expectedBudgetAmount = fipeValueCar.multiply(BigDecimal.valueOf(0.06));

        assertEquals(expectedBudgetAmount, insuranceService.createInsurance(budgetRequest).getValue());
    }

    @Test
    @DisplayName("Return budget percentage with only new driver risk")
    void createInsuranceWithNewDriverRiskOnly() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerName("Custom customer name")
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(2003, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(1996, 1, 1))
                        .driverName("other driver name")
                        .build()))
                .build();
        BigDecimal fipeValueCar = new BigDecimal(40000);
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.of(Cars.builder().fipeValue(fipeValueCar).build()));
        BigDecimal expectedBudgetAmount = fipeValueCar.multiply(BigDecimal.valueOf(0.08));

        assertEquals(expectedBudgetAmount, insuranceService.createInsurance(budgetRequest).getValue());
    }

    @Test
    @DisplayName("Return budget percentage with only driver with claims")
    void createInsuranceWithDriverWithClaimsOnly() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerName("Custom customer name")
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1996, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(2005, 1, 1))
                        .driverName("other driver name")
                        .build()))
                .build();
        BigDecimal fipeValueCar = new BigDecimal(40000);
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.of(Cars.builder().fipeValue(fipeValueCar).build()));
        when(claimsRepository.existsClaimByDriverDocument(budgetRequest.getMainDriverDocument())).thenReturn(true);
        BigDecimal expectedBudgetAmount = fipeValueCar.multiply(BigDecimal.valueOf(0.08));

        assertEquals(expectedBudgetAmount, insuranceService.createInsurance(budgetRequest).getValue());
    }

    @Test
    @DisplayName("Return budget percentage with only car with claims")
    void createInsuranceWithCarWithClaimsOnly() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerName("Custom customer name")
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1996, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(2005, 1, 1))
                        .driverName("other driver name")
                        .build()))
                .build();
        BigDecimal fipeValueCar = new BigDecimal(40000);
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.of(Cars.builder().fipeValue(fipeValueCar).build()));
        when(claimsRepository.existsClaimsByCarId(budgetRequest.getCarId())).thenReturn(true);
        BigDecimal expectedBudgetAmount = fipeValueCar.multiply(BigDecimal.valueOf(0.08));

        assertEquals(expectedBudgetAmount, insuranceService.createInsurance(budgetRequest).getValue());
    }

    @Test
    void updateInsurance() {
    }

    @Test
    void deleteInsurance() {
    }
}