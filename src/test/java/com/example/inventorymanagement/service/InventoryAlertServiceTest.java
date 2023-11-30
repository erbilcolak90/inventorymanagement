package com.example.inventorymanagement.service;


import com.example.inventorymanagement.model.entities.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InventoryAlertServiceTest {

    @InjectMocks
    private InventoryAlertService inventoryAlertService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testHandleInventoryUpdateEvent_aboveCriticalLevel() {

        Product product = new Product();
        product.setId(2);
        product.setQuantity(15);
        product.setCriticalLevel(10);

        inventoryAlertService.handleInventoryUpdateEvent(product);
    }

    @AfterEach
    void tearDown() {
    }

}
