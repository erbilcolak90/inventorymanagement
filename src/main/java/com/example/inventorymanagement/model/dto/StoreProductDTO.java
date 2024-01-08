package com.example.inventorymanagement.model.dto;

import com.example.inventorymanagement.model.enums.Cities;
import com.example.inventorymanagement.model.enums.Regions;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class StoreProductDTO {

    private int id;
    private String name;
    private String address;
    private Regions region;
    private Cities city;
    private int quantity;
    private int product_id;

    public StoreProductDTO(int id, String name, String address, Regions region, Cities city, int quantity, int product_id) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.region = region;
        this.city = city;
        this.quantity = quantity;
        this.product_id = product_id;
    }
}
