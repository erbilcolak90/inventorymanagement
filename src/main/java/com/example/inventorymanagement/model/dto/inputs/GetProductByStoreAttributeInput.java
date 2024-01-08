package com.example.inventorymanagement.model.dto.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetProductByStoreAttributeInput {

    @Min(value = 0)
    @Max(value = Integer.MAX_VALUE)
    private int productId;

    @NotBlank
    @NotEmpty
    @Size(min = 2,max = 20)
    private String filterField;
}
