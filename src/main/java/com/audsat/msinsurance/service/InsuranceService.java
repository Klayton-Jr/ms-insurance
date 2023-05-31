package com.audsat.msinsurance.service;

import com.audsat.msinsurance.dto.BudgetDTO;
import com.audsat.msinsurance.dto.request.NewBudegetRequest;

public interface InsuranceService {

    BudgetDTO createInsurance(NewBudegetRequest budegetRequest);
    void updateInsurance();
    void deleteInsurance();
}
