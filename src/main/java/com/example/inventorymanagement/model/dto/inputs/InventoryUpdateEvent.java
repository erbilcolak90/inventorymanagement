package com.example.inventorymanagement.model.dto.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryUpdateEvent {

    private int productId;
    private int quantity;
    private int criticalLevel;
}
