package com.example.inventorymanagement.model.dto.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProductInStoreInput {

    @Min(value = 0)
    @Max(value = Integer.MAX_VALUE)
    private int storeId;
    @Min(value = 0)
    @Max(value = Integer.MAX_VALUE)
    private int productId;
}
