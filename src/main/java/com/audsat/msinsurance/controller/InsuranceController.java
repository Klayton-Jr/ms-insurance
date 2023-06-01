package com.audsat.msinsurance.controller;

import com.audsat.msinsurance.dto.request.NewBudgetRequest;
import com.audsat.msinsurance.dto.request.UpdateBudgetRequest;
import com.audsat.msinsurance.dto.response.ResponseWrapper;
import com.audsat.msinsurance.service.InsuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/insurance", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;

    @PostMapping("/budget")
    public ResponseEntity<ResponseWrapper> createBudget(@Valid @RequestBody NewBudgetRequest budgetRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Success")
                        .budget(insuranceService.createInsurance(budgetRequest))
                .build());
    }

    @GetMapping("/budget/{insuranceId}")
    public ResponseEntity<ResponseWrapper> returnBudget(@PathVariable("insuranceId") final Long insuranceId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseWrapper.builder()
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .budget(insuranceService.returnInsurance(insuranceId))
                        .build());
    }

    @PatchMapping("/budget/{insuranceId}")
    public ResponseEntity<ResponseWrapper> updateBudget(@PathVariable("insuranceId") final Long insuranceId, @Valid @RequestBody UpdateBudgetRequest updateBudgetRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseWrapper.builder()
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .budget(insuranceService.updateInsurance(insuranceId, updateBudgetRequest))
                        .build());
    }

    @DeleteMapping("/budget/{insuranceId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable("insuranceId") final Long insuranceId) {
        insuranceService.deleteInsurance(insuranceId);
        return ResponseEntity.noContent().build();
    }

}
