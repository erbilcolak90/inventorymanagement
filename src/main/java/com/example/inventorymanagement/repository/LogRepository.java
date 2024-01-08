package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.model.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntity, Integer> {

}
