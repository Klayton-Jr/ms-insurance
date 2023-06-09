package com.audsat.msinsurance.service.impl;

import com.audsat.msinsurance.dto.DriverDTO;
import com.audsat.msinsurance.dto.request.NewBudgetRequest;
import com.audsat.msinsurance.dto.request.UpdateBudgetRequest;
import com.audsat.msinsurance.exception.CarNotFoundException;
import com.audsat.msinsurance.exception.InsuranceNotFoundException;
import com.audsat.msinsurance.exception.MainDriverNotFoundException;
import com.audsat.msinsurance.exception.MinorCustomerException;
import com.audsat.msinsurance.model.*;
import com.audsat.msinsurance.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsuranceServiceImplTest {

    @InjectMocks
    InsuranceServiceImpl insuranceService;
    @Mock
    CarsRepository carsRepository;
    @Mock
    ClaimsRepository claimsRepository;
    @Mock
    DriversRepository driversRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    InsurancesRepository insurancesRepository;
    @Mock
    CarDriversRepository carDriversRepository;


    @Test
    @DisplayName("Return CarNotFoundException if carId didn't return any record")
    void createInsuranceWithNoCar() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(0L)
                .customerId(0L)
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1999, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(1996, 1, 1))
                        .build()))
                .build();
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.empty());
        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
        //when(driversRepository.findByDocumentAndBirthDate(anyString(), any())).thenReturn(Optional.of(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build()));
        assertThrows(CarNotFoundException.class, () -> insuranceService.createInsurance(budgetRequest));
    }

    @Test
    @DisplayName("Return MinorCustomerException if main driver is minor")
    void createInsuranceWithMainMinorDriver() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerId(0L)
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(2020, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(1996, 1, 1))
                        .build()))
                .build();
        assertThrows(MinorCustomerException.class, () -> insuranceService.createInsurance(budgetRequest));
    }

    @Test
    @DisplayName("Return MinorCustomerException if another driver is minor")
    void createInsuranceWithAnotherMinorDriver() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerId(0L)
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1999, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(2020, 1, 1))
                        .build()))
                .build();
        assertThrows(MinorCustomerException.class, () -> insuranceService.createInsurance(budgetRequest));
    }

    @Test
    @DisplayName("Return minimum budget percentage")
    void createInsuranceWithNoRiskMainDriver() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerId(0L)
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1980, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(1996, 1, 1))
                        .build()))
                .build();
        BigDecimal fipeValueCar = new BigDecimal(40000);
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.of(Cars.builder().fipeValue(fipeValueCar).build()));
        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
        when(driversRepository.findByDocumentAndBirthDate(anyString(), any())).thenReturn(Optional.of(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build()));
        when(insurancesRepository.save(any())).thenReturn(Insurances.builder().id(1L).build());
        BigDecimal expectedBudgetAmount = fipeValueCar.multiply(BigDecimal.valueOf(0.06)).setScale(2, RoundingMode.HALF_UP);

        when(insurancesRepository.save(any())).thenReturn(
                Insurances.builder()
                        .id(1L)
                        .cars(new Cars()).customer(Customer.builder()
                                .drivers(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build())
                                .build())
                        .cars(Cars.builder().carDriversList(
                                        List.of(CarDrivers.builder()
                                                        .drivers(Drivers.builder()
                                                                .customer(List.of(Customer.builder().name("name").build()))
                                                                .build())
                                                        .mainDriver(false)
                                                        .build(),
                                                CarDrivers.builder()
                                                        .drivers(Drivers.builder()
                                                                .customer(List.of(Customer.builder().name("name").build()))
                                                                .build())
                                                        .mainDriver(true)
                                                        .build()
                                        ))
                                .build())
                        .amount(expectedBudgetAmount)
                        .build());

        assertEquals(expectedBudgetAmount, insuranceService.createInsurance(budgetRequest).getValue());
    }

    @Test
    @DisplayName("Return budget percentage with only new driver risk")
    void createInsuranceWithNewDriverRiskOnly() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerId(0L)
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(2003, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(1996, 1, 1))
                        .build()))
                .build();
        BigDecimal fipeValueCar = new BigDecimal(40000);
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.of(Cars.builder().fipeValue(fipeValueCar).build()));
        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
        when(driversRepository.findByDocumentAndBirthDate(anyString(), any())).thenReturn(Optional.of(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build()));
        when(insurancesRepository.save(any())).thenReturn(Insurances.builder().id(1L).build());
        BigDecimal expectedBudgetAmount = fipeValueCar.multiply(BigDecimal.valueOf(0.08)).setScale(2, RoundingMode.HALF_UP);

        when(insurancesRepository.save(any())).thenReturn(
                Insurances.builder()
                        .id(1L)
                        .cars(new Cars()).customer(Customer.builder()
                                .drivers(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build())
                                .build())
                        .cars(Cars.builder().carDriversList(
                                        List.of(CarDrivers.builder()
                                                        .drivers(Drivers.builder()
                                                                .customer(List.of(Customer.builder().name("name").build()))
                                                                .build())
                                                        .mainDriver(false)
                                                        .build(),
                                                CarDrivers.builder()
                                                        .drivers(Drivers.builder()
                                                                .customer(List.of(Customer.builder().name("name").build()))
                                                                .build())
                                                        .mainDriver(true)
                                                        .build()
                                        ))
                                .build())
                        .amount(expectedBudgetAmount)
                        .build());

        assertEquals(expectedBudgetAmount, insuranceService.createInsurance(budgetRequest).getValue());
    }

    @Test
    @DisplayName("Return budget percentage with only driver with claims")
    void createInsuranceWithDriverWithClaimsOnly() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerId(0L)
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1996, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(2005, 1, 1))
                        .build()))
                .build();
        BigDecimal fipeValueCar = new BigDecimal(40000);
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.of(Cars.builder().fipeValue(fipeValueCar).build()));
        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
        when(driversRepository.findByDocumentAndBirthDate(anyString(), any())).thenReturn(Optional.of(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build()));
        when(claimsRepository.existsClaimByDriverDocument(budgetRequest.getMainDriverDocument())).thenReturn(true);
        when(insurancesRepository.save(any())).thenReturn(Insurances.builder().id(1L).build());
        BigDecimal expectedBudgetAmount = fipeValueCar.multiply(BigDecimal.valueOf(0.08)).setScale(2, RoundingMode.HALF_UP);

        when(insurancesRepository.save(any())).thenReturn(
                Insurances.builder()
                        .id(1L)
                        .cars(new Cars()).customer(Customer.builder()
                                .drivers(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build())
                                .build())
                        .cars(Cars.builder().carDriversList(
                                        List.of(CarDrivers.builder()
                                                        .drivers(Drivers.builder()
                                                                .customer(List.of(Customer.builder().name("name").build()))
                                                                .build())
                                                        .mainDriver(false)
                                                        .build(),
                                                CarDrivers.builder()
                                                        .drivers(Drivers.builder()
                                                                .customer(List.of(Customer.builder().name("name").build()))
                                                                .build())
                                                        .mainDriver(true)
                                                        .build()
                                        ))
                                .build())
                        .amount(expectedBudgetAmount)
                        .build());

        assertEquals(expectedBudgetAmount, insuranceService.createInsurance(budgetRequest).getValue());
    }

    @Test
    @DisplayName("Return budget percentage with only car with claims")
    void createInsuranceWithCarWithClaimsOnly() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerId(0L)
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1996, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(2005, 1, 1))
                        .build()))
                .build();
        BigDecimal fipeValueCar = new BigDecimal(40000);
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.of(Cars.builder().fipeValue(fipeValueCar).build()));
        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
        when(driversRepository.findByDocumentAndBirthDate(anyString(), any())).thenReturn(Optional.of(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build()));
        when(claimsRepository.existsClaimsByCarId(budgetRequest.getCarId())).thenReturn(true);
        when(claimsRepository.existsClaimByDriverDocument(budgetRequest.getMainDriverDocument())).thenReturn(true);

        BigDecimal expectedBudgetAmount = fipeValueCar.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
        Drivers drivers = new Drivers();
        when(insurancesRepository.save(any())).thenReturn(
                Insurances.builder()
                        .id(1L)
                        .cars(new Cars()).customer(Customer.builder()
                                .drivers(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build())
                                .build())
                        .cars(Cars.builder().carDriversList(
                                        List.of(CarDrivers.builder()
                                                        .drivers(Drivers.builder()
                                                                .customer(List.of(Customer.builder().name("name").build()))
                                                                .build())
                                                        .mainDriver(false)
                                                        .build(),
                                                CarDrivers.builder()
                                                        .drivers(Drivers.builder()
                                                                .customer(List.of(Customer.builder().name("name").build()))
                                                                .build())
                                                        .mainDriver(true)
                                                        .build()
                                        ))
                                .build())
                        .amount(expectedBudgetAmount)
                        .build());

        assertEquals(expectedBudgetAmount, insuranceService.createInsurance(budgetRequest).getValue());
    }

    @Test
    @DisplayName("Return budget percentage with car and driver with claims")
    void createInsuranceWithCarAndDriverClaims() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerId(0L)
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(1996, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(2005, 1, 1))
                        .build()))
                .build();
        BigDecimal fipeValueCar = new BigDecimal(40000);
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.of(Cars.builder().fipeValue(fipeValueCar).build()));
        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
        when(driversRepository.findByDocumentAndBirthDate(anyString(), any())).thenReturn(Optional.of(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build()));
        when(claimsRepository.existsClaimsByCarId(budgetRequest.getCarId())).thenReturn(true);
        when(claimsRepository.existsClaimByDriverDocument(budgetRequest.getMainDriverDocument())).thenReturn(true);

        BigDecimal expectedBudgetAmount = fipeValueCar.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
        when(insurancesRepository.save(any())).thenReturn(
                Insurances.builder()
                        .id(1L)
                        .cars(new Cars()).customer(Customer.builder()
                                .drivers(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build())
                                .build())
                        .cars(Cars.builder().carDriversList(
                                        List.of(CarDrivers.builder()
                                                        .drivers(Drivers.builder()
                                                                .customer(List.of(Customer.builder().name("name").build()))
                                                                .build())
                                                        .mainDriver(false)
                                                        .build(),
                                                CarDrivers.builder()
                                                        .drivers(Drivers.builder()
                                                                .customer(List.of(Customer.builder().name("name").build()))
                                                                .build())
                                                        .mainDriver(true)
                                                        .build()
                                        ))
                                .build())
                        .amount(expectedBudgetAmount)
                        .build());

        assertEquals(expectedBudgetAmount, insuranceService.createInsurance(budgetRequest).getValue());
    }

    @Test
    @DisplayName("Return budget percentage with car and main driver with claims and new main driver")
    void createInsuranceWithCarAndDriverClaimsAndNewMainDriver() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerId(0L)
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(2005, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(1999, 1, 1))
                        .build()))
                .build();
        BigDecimal fipeValueCar = new BigDecimal(40000);
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.of(Cars.builder().fipeValue(fipeValueCar).build()));
        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
        when(driversRepository.findByDocumentAndBirthDate(anyString(), any())).thenReturn(Optional.of(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build()));
        when(claimsRepository.existsClaimsByCarId(budgetRequest.getCarId())).thenReturn(true);
        when(claimsRepository.existsClaimByDriverDocument(budgetRequest.getMainDriverDocument())).thenReturn(true);
        when(insurancesRepository.save(any())).thenReturn(Insurances.builder().id(1L).build());
        BigDecimal expectedBudgetAmount = fipeValueCar.multiply(BigDecimal.valueOf(0.12)).setScale(2, RoundingMode.HALF_UP);

        when(insurancesRepository.save(any())).thenReturn(
                Insurances.builder()
                        .id(1L)
                        .cars(new Cars()).customer(Customer.builder()
                                .drivers(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build())
                                .build())
                        .cars(Cars.builder().carDriversList(
                                        List.of(CarDrivers.builder()
                                                        .drivers(Drivers.builder()
                                                                .customer(List.of(Customer.builder().name("name").build()))
                                                                .build())
                                                        .mainDriver(false)
                                                        .build(),
                                                CarDrivers.builder()
                                                        .drivers(Drivers.builder()
                                                                .customer(List.of(Customer.builder().name("name").build()))
                                                                .build())
                                                        .mainDriver(true)
                                                        .build()
                                        ))
                                .build())
                        .amount(expectedBudgetAmount)
                        .build());

        assertEquals(expectedBudgetAmount, insuranceService.createInsurance(budgetRequest).getValue());
    }

    @Test
    @DisplayName("Return MainDriverNotFoundException if main driver does not exists")
    void createInsuranceWithMainDriverNotExisting() {
        NewBudgetRequest budgetRequest = NewBudgetRequest.builder()
                .carId(1L)
                .customerId(0L)
                .mainDriverDocument("CNH-123")
                .mainDriverBirthDate(LocalDate.of(2005, 1, 1))
                .drivers(List.of(DriverDTO.builder()
                        .driverBirthDate(LocalDate.of(1999, 1, 1))
                        .build()))
                .build();
        when(carsRepository.findById(budgetRequest.getCarId())).thenReturn(Optional.of(Cars.builder().build()));
        when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));

        assertThrows(MainDriverNotFoundException.class, () -> insuranceService.createInsurance(budgetRequest));
    }

    @Test
    @DisplayName("Return InsuranceNotFoundException if budget insuranceId does not exists")
    void returnInsuranceWithInexistingInsuranceId() {
        Long insuranceId = 0L;
        assertThrows(InsuranceNotFoundException.class, () -> insuranceService.returnInsurance(insuranceId));
    }

    @Test
    @DisplayName("Return Insurance for existing by insuranceId")
    void returnInsuranceWithExistingInsuranceId() {
        Long insuranceId = 0L;
        when(insurancesRepository.findById(0L)).thenReturn(Optional.of(Insurances.builder()
                .id(1L)
                .cars(new Cars()).customer(Customer.builder()
                        .drivers(Drivers.builder().customer(List.of(Customer.builder().name("name").build())).build())
                        .build())
                .cars(Cars.builder().carDriversList(
                                List.of(CarDrivers.builder()
                                                .drivers(Drivers.builder()
                                                        .customer(List.of(Customer.builder().name("name").build()))
                                                        .build())
                                                .mainDriver(false)
                                                .build(),
                                        CarDrivers.builder()
                                                .drivers(Drivers.builder()
                                                        .customer(List.of(Customer.builder().name("name").build()))
                                                        .build())
                                                .mainDriver(true)
                                                .build()
                                ))
                        .build())
                .build()));
        assertDoesNotThrow(() -> insuranceService.returnInsurance(insuranceId));
    }

    @Test
    @DisplayName("Return InsuranceNotFoundException if budget insuranceId does not exists")
    void updateInsuranceWithInexistingInsuranceId() {
        assertThrows(InsuranceNotFoundException.class, () -> insuranceService.updateInsurance(0L, any()));
    }

    @Test
    @DisplayName("Return InsuranceNotFoundException if budget insuranceId does not exists")
    void deleteInsuranceWithInexistingInsuranceId() {
        assertThrows(InsuranceNotFoundException.class, () -> insuranceService.deleteInsurance(0L));
    }
}