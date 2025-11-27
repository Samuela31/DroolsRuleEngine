package com.example.validationengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.validationengine.entity.Fund;

@Repository
public interface FundRepository extends JpaRepository<Fund, Integer> {
}
