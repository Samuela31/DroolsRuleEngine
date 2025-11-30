package com.example.validationengine.controller;


import com.example.validationengine.dto.ValidationResult;
// import com.example.validationengine.dto.ValidationResultDTO;
import com.example.validationengine.entity.CanonicalTrade;
import com.example.validationengine.service.SimulationService;
// import com.example.validationengine.entity.ValidationEntity;
import com.example.validationengine.service.ValidationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validations")
public class ValidationController {

    @Autowired
    private  SimulationService simulationService;

    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping("/trade")
    public ResponseEntity<ValidationResult> validateTrade(@RequestBody CanonicalTrade data) {
        ValidationResult result = validationService.validate(data);
        if (result.isValid()) {
            data.setStatus("VALID");
            validationService.storeValidOrders(data);
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
        }
        data.setStatus("INVALID");
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(result);
    }


    @GetMapping("/simulate/{count}")
    public String simulate(@PathVariable int count) {

        long start = System.currentTimeMillis();

        simulationService.generateTrades(count);

        long end = System.currentTimeMillis();
        long totalMs = end - start;

        return count + " trades simulated in " + totalMs + " ms (" + (totalMs / 1000.0) + " seconds)";
    }


}

