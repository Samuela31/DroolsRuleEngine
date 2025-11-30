package com.example.validationengine.dto;

import lombok.Data;

@Data
public class ClientDTO {
    private Integer clientId;
    private String kycStatus;
    private String panNumber;
    private String status;
    private String type;

    // getters + setters
}

