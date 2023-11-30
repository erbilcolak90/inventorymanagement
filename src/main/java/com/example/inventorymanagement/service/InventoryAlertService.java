package com.example.inventorymanagement.service;

import com.example.inventorymanagement.model.entities.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InventoryAlertService {
    @EventListener
    public void handleInventoryUpdateEvent(Product product) {
        int quantity = product.getQuantity();
        int criticalLevel = product.getCriticalLevel();
        int productId = product.getId();

        if (quantity < criticalLevel) {
            logCriticalLevelEvent(productId);
        }
    }

    private void logCriticalLevelEvent(int productId) {
        // log info must be seems at terminal
        log.info("Inventory for product " + productId + " is below the critical level!");
    }
}
