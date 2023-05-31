package com.audsat.msinsurance.service.impl;

import com.audsat.msinsurance.dto.request.NewBudegetRequest;
import com.audsat.msinsurance.exception.CarNotFoundException;
import com.audsat.msinsurance.model.Cars;
import com.audsat.msinsurance.repository.CarsRepository;
import com.audsat.msinsurance.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {
    private final CarsRepository carsRepository;
    @Override
    public void createInsurance(NewBudegetRequest budegetRequest) {
        Cars cars = getCarById(budegetRequest.getCarId());
    }

    private Cars getCarById(Long cardId) {
        return carsRepository.findById(cardId).orElseThrow(() -> new CarNotFoundException(cardId));
    }

    @Override
    public void updateInsurance() {

    }

    @Override
    public void deleteInsurance() {

    }
}