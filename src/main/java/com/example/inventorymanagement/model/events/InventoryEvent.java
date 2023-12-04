package com.example.inventorymanagement.model.events;

import com.example.inventorymanagement.model.entities.Product;
import org.springframework.context.ApplicationEvent;

public class InventoryEvent extends ApplicationEvent {

    private Product product;

    public InventoryEvent(Object source, Product product) {
        super(source);
        this.product = product;
    }
}
