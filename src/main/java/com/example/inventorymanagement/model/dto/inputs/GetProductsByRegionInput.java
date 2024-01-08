package com.example.inventorymanagement.model.dto.inputs;

import com.example.inventorymanagement.model.enums.Regions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProductsByRegionInput {

    private Regions region;
    private int productId;
}
