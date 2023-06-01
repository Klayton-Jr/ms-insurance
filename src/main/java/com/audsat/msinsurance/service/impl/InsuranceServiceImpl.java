package com.audsat.msinsurance.service.impl;

import com.audsat.msinsurance.dto.BudgetDTO;
import com.audsat.msinsurance.dto.CarDTO;
import com.audsat.msinsurance.dto.DriverDTO;
import com.audsat.msinsurance.dto.request.NewBudgetRequest;
import com.audsat.msinsurance.exception.CarNotFoundException;
import com.audsat.msinsurance.exception.MainDriverNotFoundException;
import com.audsat.msinsurance.exception.MinorCustomerException;
import com.audsat.msinsurance.model.*;
import com.audsat.msinsurance.repository.*;
import com.audsat.msinsurance.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {
    private final CarsRepository carsRepository;
    private final ClaimsRepository claimsRepository;
    private final DriversRepository driversRepository;
    private final CustomerRepository customerRepository;
    private final InsurancesRepository insurancesRepository;

    @Override
    public BudgetDTO createInsurance(NewBudgetRequest budgetRequest) {
        validate(budgetRequest);

        Cars car = getCarById(budgetRequest.getCarId());
        Drivers mainDriver = validateCustomerAndMainDriver(budgetRequest);

        saveOthersDriversIfNotExists(budgetRequest.getDrivers(), car);
        BigDecimal insuranceAmount = calculateInsuranceAmount(budgetRequest, car.getFipeValue());


        return saveNewInsuranceBudget(budgetRequest, insuranceAmount, car, mainDriver);
    }

    private BudgetDTO saveNewInsuranceBudget(NewBudgetRequest budgetRequest, BigDecimal insuranceAmount, Cars car, Drivers mainDriver) {
        Insurances insurances = insurancesRepository.save(Insurances.builder()
                .customer(mainDriver.getCustomer())
                .active(true)
                .cars(car)
                .amount(insuranceAmount)
                .build());

        return BudgetDTO.builder()
                .car(CarDTO.of(car))
                .mainDriverName(budgetRequest.getCustomerName())
                .mainDriverDocument(budgetRequest.getMainDriverDocument())
                .otherDrivers(budgetRequest.getDrivers())
                .value(insuranceAmount)
                .insuranceId(insurances.getId())
                .build();
    }

    private BigDecimal calculateInsuranceAmount(NewBudgetRequest budgetRequest, BigDecimal fipeValueCar) {
        int percentageRisk = 6;
        percentageRisk += calculateIfNewDriver(budgetRequest.getMainDriverBirthDate());
        percentageRisk += calculateIfMainDriverHaveClaim(budgetRequest.getMainDriverDocument());
        percentageRisk += calculateIfCarHaveClaim(budgetRequest.getCarId());
        return fipeValueCar.multiply(BigDecimal.valueOf((double) percentageRisk / 100)).setScale(2, RoundingMode.HALF_UP);
    }

    private Integer calculateIfNewDriver(LocalDate mainDriverBirthDate) {
        if (Period.between(mainDriverBirthDate, LocalDate.now()).getYears() <= 25)
            return 2;
        return 0;
    }

    private Integer calculateIfMainDriverHaveClaim(String mainDriverDocument) {
        if (claimsRepository.existsClaimByDriverDocument(mainDriverDocument))
            return 2;
        return 0;
    }

    private Integer calculateIfCarHaveClaim(Long carId) {
        if (claimsRepository.existsClaimsByCarId(carId))
            return 2;
        return 0;
    }

    private void validate(NewBudgetRequest budgetRequest) {
        validateDrivers(budgetRequest);
    }

    private Drivers validateCustomerAndMainDriver(NewBudgetRequest budgetRequest) {
        return validateMainDriverAndUpdate(budgetRequest);
    }

    private void saveOthersDriversIfNotExists(List<DriverDTO> driverDTOList, Cars car) {
        driverDTOList.forEach(driverDTO -> saveNewNotMainDriversWithCarId(driverDTO, car));
    }

    private void saveNewNotMainDriversWithCarId(DriverDTO driverDTO, Cars car) {
        customerRepository.save(Customer.builder()
                .name(driverDTO.getDriverName())
                .drivers(Drivers.builder()
                        .birthdate(driverDTO.getDriverBirthDate())
                        .document(driverDTO.getDriverDocument())
                        .carDriversList(
                                List.of(CarDrivers.builder()
                                        .cars(car)
                                        .mainDriver(false)
                                        .build()))
                        .build())
                .build());
    }

    private Drivers validateMainDriverAndUpdate(NewBudgetRequest budgetRequest) {
        Optional<Drivers> optionalMainDriver = driversRepository.findByDocumentAndBirthDate(budgetRequest.getMainDriverDocument(), budgetRequest.getMainDriverBirthDate());
        if (optionalMainDriver.isEmpty())
            throw new MainDriverNotFoundException(budgetRequest.getMainDriverDocument());

        Drivers mainDriver = optionalMainDriver.get();
        if (!mainDriver.getCustomer().getName().equalsIgnoreCase(budgetRequest.getCustomerName())) {
            mainDriver.getCustomer().setName(budgetRequest.getCustomerName());
            driversRepository.save(mainDriver);
        }
        return mainDriver;
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
