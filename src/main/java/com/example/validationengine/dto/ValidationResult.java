package com.example.validationengine.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ValidationResult {
    private boolean valid = true;
    private final List<String> errors = new ArrayList<>();

    public List<String> getErrors() { return errors; }

    public void addError(String error) {
        this.valid = false;
        this.errors.add(error);
    }
}
