package com.example.inventorymanagement.model.dto.inputs;

import com.example.inventorymanagement.model.enums.Cities;
import com.example.inventorymanagement.model.enums.Regions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateStoreInput {

    @NotEmpty(message = "Name may not be empty")
    @NotBlank(message = "Name may not be blank")
    @Size(min = 2, max = 50)
    private String name;

    @NotEmpty(message = "Address may not be empty")
    @NotBlank(message = "Address may not be blank")
    @Size(min = 2, max = 250)
    private String address;
    private Regions region;
    private Cities city;
}
