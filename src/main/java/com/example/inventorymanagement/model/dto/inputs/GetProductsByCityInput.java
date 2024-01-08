package com.example.inventorymanagement.model.dto.inputs;

import com.example.inventorymanagement.model.enums.Cities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProductsByCityInput {

    private Cities city;
    private int productId;
}
