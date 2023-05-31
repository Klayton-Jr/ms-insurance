package com.audsat.msinsurance.controller;

import com.audsat.msinsurance.dto.request.NewBudegetRequest;
import com.audsat.msinsurance.service.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insurance")
@RequiredArgsConstructor
public class InsuranceController {

    private final InsuranceService insuranceService;

    @PostMapping("/budget")
    public ResponseEntity<Void> createBudget(@RequestBody NewBudegetRequest budegetRequest) {
        insuranceService.createInsurance(budegetRequest);
        return null;
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
