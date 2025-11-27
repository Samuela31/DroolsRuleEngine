package com.example.validationengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.validationengine.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
}
