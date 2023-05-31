package com.audsat.msinsurance.service;

import com.audsat.msinsurance.dto.request.NewBudegetRequest;

public interface InsuranceService {

    void createInsurance(NewBudegetRequest budegetRequest);
    void updateInsurance();
    void deleteInsurance();
}
