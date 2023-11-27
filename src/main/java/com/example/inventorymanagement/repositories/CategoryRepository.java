package com.example.inventorymanagement.repositories;

import com.example.inventorymanagement.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
