package com.audsat.msinsurance.service;

import com.audsat.msinsurance.dto.BudgetDTO;
import com.audsat.msinsurance.dto.request.NewBudgetRequest;
import com.audsat.msinsurance.dto.request.UpdateBudgetRequest;

public interface InsuranceService {

    BudgetDTO returnInsurance(Long id);
    BudgetDTO createInsurance(NewBudgetRequest budegetRequest);
    BudgetDTO updateInsurance(Long id, UpdateBudgetRequest updateBudgetRequest);
    void deleteInsurance(Long id);
}
