package com.example.inventorymanagement.model.dto.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductInput{

    @NotEmpty
    @NotBlank
    @Min(value = 0)
    @Max(value = Integer.MAX_VALUE)
    private int id;

    private String name;

    @Min(value = 0, message = "Category Id must be greater than or equal to 0")
    @Max(value = Integer.MAX_VALUE, message = "Category Id must be less than or equal to 150000")
    private int categoryId;

    @Min(value = 0, message = "Product quantity must be greater than or equal to 0")
    @Max(value = Integer.MAX_VALUE, message = "Category Id must be less than or equal to 25000")
    private int quantity;

    @Min(value = 0, message = "Critical level must be greater than or equal to 0")
    @Max(value = 100, message = "Critical level must be less than or equal to 100")
    private int criticalLevel;

}
