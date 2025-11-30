package com.example.validationengine.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FundDTO {
    private Integer fundId;
    private String schemeCode;
    private String status;
    private BigDecimal minLimit;
    private BigDecimal maxLimit;
}