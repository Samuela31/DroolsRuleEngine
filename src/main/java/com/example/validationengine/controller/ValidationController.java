package com.example.validationengine.controller;


import com.example.validationengine.dto.ValidationResult;
// import com.example.validationengine.dto.ValidationResultDTO;
import com.example.validationengine.entity.CanonicalTrade;
// import com.example.validationengine.entity.ValidationEntity;
import com.example.validationengine.service.ValidationService;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validations")
public class ValidationController {

    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    // @PostMapping("/trade")
    // public ResponseEntity<ValidationResult> validateTrade(@RequestBody CanonicalTrade ct) {
    //     return ResponseEntity.ok(validationService.validate(ct));
    // }
    @PostMapping("/trade")
    public ResponseEntity<ValidationResult> validateTrade(@RequestBody CanonicalTrade ct) {
        ValidationResult result = validationService.validate(ct);
        if (result.isValid()) {
            ct.setStatus("VALID");
            validationService.storeValidOrders(ct);
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
        }
        ct.setStatus("INVALID");
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(result);
    }

}

