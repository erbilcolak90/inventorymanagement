package com.example.inventorymanagement.repository;

import com.example.inventorymanagement.model.dto.payloads.StoreProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreProductsRepository extends JpaRepository<StoreProducts,Integer> {

    @Modifying
    @Query("UPDATE StoreProduct sp SET sp.quantity = sp.quantity + :quantity " +
            "WHERE sp.store.id = :storeId AND sp.product.id = :productId")
    boolean updateStoreProductQuantity(@Param("storeId") int storeId,
                                    @Param("productId") int productId,
                                    @Param("quantity") int quantity);

    @Modifying
    @Query("UPDATE StoreProduct sp SET sp.quantity = sp.quantity - :quantity " +
            "WHERE sp.store.id = :storeId AND sp.product.id = :productId " +
            "AND sp.quantity >= :quantity")
    boolean removeProductInStore(@Param("storeId") int storeId,
                              @Param("productId") int productId,
                              @Param("quantity") int quantity);
}
