package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.model.entities.StoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreProductRepository extends JpaRepository<StoreProduct,Integer> {
    @Query(value = "SELECT * FROM StoreProducts sp WHERE sp.store_id = :store_id AND sp.product_id = :product_id", nativeQuery = true)
    Optional<StoreProduct> findByStore_idAndProduct_id(@Param("store_id") int store_id, @Param("product_id") int product_id);

    @Query("SELECT sp FROM StoreProduct sp WHERE sp.store_id IN :storeIds AND sp.product_id= :product_id")
    List<StoreProduct> findAllByIdIn(@Param("storeIds") List<Integer> storeIds,@Param("product_id") int product_id);

}
