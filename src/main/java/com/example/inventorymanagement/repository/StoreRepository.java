package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.model.entities.Store;
import com.example.inventorymanagement.model.enums.Cities;
import com.example.inventorymanagement.model.enums.Regions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    Store findByName(String name);
    @Query("SELECT s.id FROM Store s WHERE s.region= :region")
    List<Integer> findByRegion(@Param("region") Regions region);
    @Query("SELECT s FROM Store s WHERE s.id IN :storeIds")
    List<Store> findAllByIdIn(@Param("storeIds") List<Integer> storeIds);
    @Query("SELECT s.id FROM Store s WHERE s.city= :city")
    List<Integer> findByCity(@Param("city") Cities city);

}
