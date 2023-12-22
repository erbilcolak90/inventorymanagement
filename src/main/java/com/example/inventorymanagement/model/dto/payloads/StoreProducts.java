package com.example.inventorymanagement.model.dto.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreProducts {

    private int id;

    private int store_id;
    private int product_id;
    private int quantity;
}
