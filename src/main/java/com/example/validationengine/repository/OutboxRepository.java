package com.example.validationengine.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.validationengine.entity.OutboxEntity;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEntity, UUID>{

}
