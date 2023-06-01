package com.audsat.msinsurance.service.impl;

import com.audsat.msinsurance.dto.BudgetDTO;
import com.audsat.msinsurance.dto.DriverDTO;
import com.audsat.msinsurance.dto.request.NewBudgetRequest;
import com.audsat.msinsurance.exception.*;
import com.audsat.msinsurance.model.*;
import com.audsat.msinsurance.repository.*;
import com.audsat.msinsurance.service.InsuranceService;
import jakarta.transaction.Transactional;
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
    private final CarDriversRepository carDriversRepository;

    @Override
    public BudgetDTO returnInsurance(Long id) {
        Optional<Insurances> optionalInsurance = insurancesRepository.findById(id);
        if (optionalInsurance.isEmpty())
            throw new InsuranceNotFoundException(id);
        Insurances insurance = optionalInsurance.get();

        return BudgetDTO.of(insurance);
    }

    @Override
    public BudgetDTO createInsurance(NewBudgetRequest budgetRequest) {
        validate(budgetRequest);

        Customer customer = getCustomerById(budgetRequest.getCustomerId());
        Cars car = getCarById(budgetRequest.getCarId());
        Drivers mainDriver = validateCustomerAndMainDriver(budgetRequest);

        saveOthersDriversIfNotExists(budgetRequest.getDrivers(), car, customer);
        BigDecimal insuranceAmount = calculateInsuranceAmount(budgetRequest, car.getFipeValue());


        return saveNewInsuranceBudget(insuranceAmount, car, mainDriver, customer);
    }

    private Customer getCustomerById(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isEmpty())
            throw new CustomerNotFoundException(id);
        return optionalCustomer.get();
    }

    private BudgetDTO saveNewInsuranceBudget(BigDecimal insuranceAmount, Cars car, Drivers mainDriver, Customer customer) {
        Insurances insurance = insurancesRepository.save(Insurances.builder()
                .customer(customer)
                .active(true)
                .cars(car)
                .amount(insuranceAmount)
                .build());

        return BudgetDTO.of(insurance);
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

    private void saveOthersDriversIfNotExists(List<DriverDTO> driverDTOList, Cars car, Customer customer) {
        driverDTOList.forEach(driverDTO -> {
            if (driversRepository.existsDriverByDocumentAndBirthDateAndCarAntNotMainDriver(driverDTO.getDriverDocument(), driverDTO.getDriverBirthDate(), car.getId()))
                return;
            saveNewNotMainDriversWithCarId(driverDTO, car, customer);
        });
    }

    @Transactional
    public void saveNewNotMainDriversWithCarId(DriverDTO driverDTO, Cars car, Customer customer) {

        Drivers drivers = driversRepository.save(Drivers.builder()
                .birthdate(driverDTO.getDriverBirthDate())
                .document(driverDTO.getDriverDocument())
                .customer(List.of(customer))
                .build());
//
//        Customer customer = customerRepository.save(Customer.builder()
//                .name(driverDTO.getDriverName())
//                .drivers(drivers)
//                .build());
        CarDrivers carDrivers = carDriversRepository.save(CarDrivers.builder()
                .cars(car)
                .mainDriver(false)
                .drivers(drivers)
                .build());
    }

    private Drivers validateMainDriverAndUpdate(NewBudgetRequest budgetRequest) {
        Optional<Drivers> optionalMainDriver = driversRepository.findByDocumentAndBirthDate(budgetRequest.getMainDriverDocument(), budgetRequest.getMainDriverBirthDate());
        if (optionalMainDriver.isEmpty())
            throw new MainDriverNotFoundException(budgetRequest.getMainDriverDocument());

        return optionalMainDriver.get();
    }

    private void validateDrivers(NewBudgetRequest budgetRequest) {
        validateIfMinor(budgetRequest.getMainDriverBirthDate(), budgetRequest.getMainDriverDocument());
        budgetRequest.getDrivers().forEach(driver -> validateIfMinor(driver.getDriverBirthDate(), driver.getDriverDocument()));
    }

    private void validateIfMinor(LocalDate birthDate, String document) {
        if (birthDate != null && isMinor(birthDate))
            throw new MinorCustomerException(document);
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
