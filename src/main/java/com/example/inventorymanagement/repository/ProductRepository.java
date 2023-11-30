package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.model.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByName(String name);

    Optional<Product> findByIdAndIsDeletedFalse(int id);

    Page<Product> findByCategoryId(int categoryId,Pageable pageable);
}
