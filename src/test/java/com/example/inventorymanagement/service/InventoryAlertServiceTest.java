package com.example.inventorymanagement.service;


import com.example.inventorymanagement.model.entities.Product;
import com.example.inventorymanagement.service.eventservice.InventoryAlertService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class InventoryAlertServiceTest {
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private InventoryAlertService inventoryAlertService;

    private final Logger logger = LoggerFactory.getLogger(InventoryAlertServiceTest.class);

    private Product testProduct;


    @DisplayName("handleInventoryUpdateEvent success and should log critical level event")
    @Test
    void handleInventoryUpdateEvent_QuantityBelowCriticalLevel_ShouldLogCriticalLevelEvent() {
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setName("testproductname");
        testProduct.setCategoryId(1);
        testProduct.setQuantity(5);
        testProduct.setCriticalLevel(10);

        inventoryAlertService.handleInventoryUpdateEvent(testProduct);
        assertTrue(logger.isInfoEnabled(),"Inventory for product " + testProduct.getId() + " is below the critical level!");
    }

    @DisplayName("handleInventoryUpdateEvent success and should not log critical level event")
    @Test
    void handleInventoryUpdateEvent_QuantityAboveCriticalLevel_ShouldNotLogCriticalLevelEvent() {
        // Arrange
        Product product = new Product();
        product.setId(2);
        product.setQuantity(15);
        product.setCriticalLevel(5);

        inventoryAlertService.handleInventoryUpdateEvent(product);
        assertTrue(logger.isInfoEnabled());
    }
}
