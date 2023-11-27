package com.example.inventorymanagement.repositories;

import com.example.inventorymanagement.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Integer> {

}
