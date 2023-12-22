package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.model.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Integer> {

    Store findByName(String name);

    @Query(value = "SELECT sp.store_id FROM store_products sp WHERE product_id = :productId", nativeQuery = true)
    List<Integer> findStoreIdsByProductId(@Param("productId") int productId);

    @Query(value = "SELECT * FROM Store s WHERE s.id IN :storeIds AND (s.name = :attribute OR s.city = :attribute OR s.region = :attribute)",nativeQuery = true)
    List<Store> findStoresByStoreIdsAndAttribute(@Param("storeIds") List<Integer> storeIds, @Param("attribute") String attribute);

    @Query("SELECT COUNT(sp) > 0 FROM Store s JOIN s.products sp WHERE s.id = :storeId AND sp.id = :productId")
    boolean existsProductInStore(@Param("storeId") int storeId, @Param("productId") int productId);

}
