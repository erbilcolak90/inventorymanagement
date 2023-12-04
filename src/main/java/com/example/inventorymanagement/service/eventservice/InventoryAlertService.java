package com.example.inventorymanagement.service.eventservice;

import com.example.inventorymanagement.model.entities.Product;
import com.example.inventorymanagement.model.events.InventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryAlertService {

    private ApplicationEventPublisher eventPublisher;
    @EventListener
    public void handleInventoryUpdateEvent(Product product) {
        int quantity = product.getQuantity();
        int criticalLevel = product.getCriticalLevel();
        int productId = product.getId();

        if (quantity < criticalLevel) {
            logCriticalLevelEvent(productId);
        }

        eventPublisher.publishEvent(new InventoryEvent(eventPublisher,product));

    }

    private void logCriticalLevelEvent(int productId) {
        // log info must be seems at terminal
        log.info("Inventory for product " + productId + " is below the critical level!");
    }
}
