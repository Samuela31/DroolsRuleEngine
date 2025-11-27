package com.example.validationengine.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "client")
public class Client {

    @Id
    @Column(name = "client_id")
    private Integer clientId;       

    @Column(name = "kyc_status")
    private String kycStatus;      

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;
}
