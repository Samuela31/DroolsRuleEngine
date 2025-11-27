package com.example.validationengine.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "fund")
public class Fund {

    @Id
    @Column(name = "fund_id")
    private Integer fundId;             

    @Column(name = "scheme_code")
    private String schemeCode;

    @Column(name = "status")
    private String status;

    @Column(name = "max_limit")
    private BigDecimal maxLimit;

    @Column(name = "min_limit")
    private BigDecimal minLimit;
}

