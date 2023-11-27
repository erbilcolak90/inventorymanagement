package com.example.inventorymanagement.repositories;

import com.example.inventorymanagement.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
