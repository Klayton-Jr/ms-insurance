package com.audsat.msinsurance.service.impl;

import com.audsat.msinsurance.dto.request.NewBudegetRequest;
import com.audsat.msinsurance.exception.CarNotFoundException;
import com.audsat.msinsurance.exception.MinorCustomerException;
import com.audsat.msinsurance.model.Cars;
import com.audsat.msinsurance.repository.CarsRepository;
import com.audsat.msinsurance.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {
    private final CarsRepository carsRepository;
    @Override
    public void createInsurance(NewBudegetRequest budegetRequest) {
        validate(budegetRequest);
        Cars cars = getCarById(budegetRequest.getCarId());
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
