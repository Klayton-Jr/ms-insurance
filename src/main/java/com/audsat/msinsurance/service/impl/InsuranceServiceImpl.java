package com.audsat.msinsurance.service.impl;

import com.audsat.msinsurance.dto.BudgetDTO;
import com.audsat.msinsurance.dto.CarDTO;
import com.audsat.msinsurance.dto.request.NewBudegetRequest;
import com.audsat.msinsurance.exception.CarNotFoundException;
import com.audsat.msinsurance.exception.MinorCustomerException;
import com.audsat.msinsurance.model.Cars;
import com.audsat.msinsurance.repository.CarsRepository;
import com.audsat.msinsurance.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {
    private final CarsRepository carsRepository;
    @Override
    public BudgetDTO createInsurance(NewBudegetRequest budegetRequest) {
        validate(budegetRequest);
        Cars cars = getCarById(budegetRequest.getCarId());

        BigDecimal insuranceAmount = calculateInsuranceAmount(budegetRequest, cars.getFipeValue());

        BudgetDTO budgetDTO = BudgetDTO.builder()
                .car(CarDTO.of(cars))
                .mainDriverName(budegetRequest.getMainDriverDocument())
                .mainDriverDocument(budegetRequest.getMainDriverDocument())
                .otherDrivers(budegetRequest.getDrivers())
                .value(insuranceAmount)
                .build();

        return budgetDTO;
    }

    private BigDecimal calculateInsuranceAmount(NewBudegetRequest budegetRequest, BigDecimal fipeValueCar) {
        Integer percentageRisk = 6;
        calculateIfNewDriver(budegetRequest.getMainDriverBirthDate(), percentageRisk);
        calculateIfMainDriverHaveClaim(budegetRequest.getMainDriverDocument(), percentageRisk);
        calculateIfCarHaveClaim(budegetRequest.getCarId(), percentageRisk);
        return fipeValueCar.multiply(BigDecimal.valueOf((double) percentageRisk / 100));
    }

    private void calculateIfNewDriver(LocalDate mainDriverBirthDate, Integer percentageRisk) {

    }

    private void calculateIfMainDriverHaveClaim(String mainDriverBirthDate, Integer percentageRisk) {

    }

    private void calculateIfCarHaveClaim(Long carId, Integer percentageRisk) {

    }

    private void validate(NewBudegetRequest budegetRequest) {
        validateDrivers(budegetRequest);
    }

    private void validateDrivers(NewBudegetRequest budegetRequest) {
        validateIfMinor(budegetRequest.getMainDriverBirthDate(), budegetRequest.getCustomerName());
        budegetRequest.getDrivers().forEach(driver -> validateIfMinor(driver.getDriverBirthDate(), driver.getDriverName()));
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
