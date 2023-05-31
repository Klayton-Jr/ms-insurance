package com.audsat.msinsurance.service.impl;

import com.audsat.msinsurance.dto.BudgetDTO;
import com.audsat.msinsurance.dto.CarDTO;
import com.audsat.msinsurance.dto.request.NewBudgetRequest;
import com.audsat.msinsurance.exception.CarNotFoundException;
import com.audsat.msinsurance.exception.MinorCustomerException;
import com.audsat.msinsurance.model.Cars;
import com.audsat.msinsurance.repository.CarsRepository;
import com.audsat.msinsurance.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {
    private final CarsRepository carsRepository;
    @Override
    public BudgetDTO createInsurance(NewBudgetRequest budgetRequest) {
        validate(budgetRequest);
        Cars cars = getCarById(budgetRequest.getCarId());

        BigDecimal insuranceAmount = calculateInsuranceAmount(budgetRequest, cars.getFipeValue());

        BudgetDTO budgetDTO = BudgetDTO.builder()
                .car(CarDTO.of(cars))
                .mainDriverName(budgetRequest.getMainDriverDocument())
                .mainDriverDocument(budgetRequest.getMainDriverDocument())
                .otherDrivers(budgetRequest.getDrivers())
                .value(insuranceAmount)
                .build();

        return budgetDTO;
    }

    private BigDecimal calculateInsuranceAmount(NewBudgetRequest budgetRequest, BigDecimal fipeValueCar) {
        int percentageRisk = 6;
        percentageRisk += calculateIfNewDriver(budgetRequest.getMainDriverBirthDate());
        percentageRisk += calculateIfMainDriverHaveClaim(budgetRequest.getMainDriverDocument());
        percentageRisk += calculateIfCarHaveClaim(budgetRequest.getCarId());
        return fipeValueCar.multiply(BigDecimal.valueOf((double) percentageRisk / 100));
    }

    private Integer calculateIfNewDriver(LocalDate mainDriverBirthDate) {
        if (Period.between(mainDriverBirthDate, LocalDate.now()).getYears() <= 25)
            return 2;
        return 0;
    }

    private Integer calculateIfMainDriverHaveClaim(String mainDriverBirthDate) {
        return 0;
    }

    private Integer calculateIfCarHaveClaim(Long carId) {
        return 0;
    }

    private void validate(NewBudgetRequest budgetRequest) {
        validateDrivers(budgetRequest);
    }

    private void validateDrivers(NewBudgetRequest budgetRequest) {
        validateIfMinor(budgetRequest.getMainDriverBirthDate(), budgetRequest.getCustomerName());
        budgetRequest.getDrivers().forEach(driver -> validateIfMinor(driver.getDriverBirthDate(), driver.getDriverName()));
    }

    private void validateIfMinor(LocalDate birthDate, String name) {
        if (birthDate != null && isMinor(birthDate))
            throw new MinorCustomerException(name);
    }

    private boolean isMinor(LocalDate birthDate) {
        return !birthDate.plusYears(18).isBefore(LocalDate.now());
    }

    private Cars getCarById(Long cardId) {
        Optional<Cars> carOptional = carsRepository.findById(cardId);
        if (carOptional.isPresent())
            return carOptional.get();
        throw new CarNotFoundException(cardId);
    }

    @Override
    public void updateInsurance() {

    }

    @Override
    public void deleteInsurance() {

    }
}
