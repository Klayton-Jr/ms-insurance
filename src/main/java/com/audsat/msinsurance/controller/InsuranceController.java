package com.audsat.msinsurance.controller;

import com.audsat.msinsurance.dto.request.NewBudgetRequest;
import com.audsat.msinsurance.dto.response.ResponseWrapper;
import com.audsat.msinsurance.service.InsuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insurance")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;

    @PostMapping(value = "/budget", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> createBudget(@Valid @RequestBody NewBudgetRequest budgetRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Sucess")
                        .budget(insuranceService.createInsurance(budgetRequest))
                .build());
    }

    @GetMapping("/budget/{insuranceId}")
    public ResponseEntity<Void> returnBudget(@PathVariable("insuranceId") final String insuranceId) {
        return null;
    }

    @PatchMapping("/budget/{insuranceId}")
    public ResponseEntity<Void> updateBudget(@PathVariable("insuranceId") final String insuranceId) {
        return null;
    }

    @DeleteMapping("/budget/{insuranceId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable("insuranceId") final String insuranceId) {
        return null;
    }

}
