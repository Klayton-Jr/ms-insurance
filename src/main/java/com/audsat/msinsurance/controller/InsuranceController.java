package com.audsat.msinsurance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insurance")
public class InsuranceController {

    @PostMapping("/budget")
    public ResponseEntity<Void> createBudget() {
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
