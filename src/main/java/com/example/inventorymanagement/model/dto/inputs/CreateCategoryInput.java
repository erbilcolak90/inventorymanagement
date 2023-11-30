package com.example.inventorymanagement.model.dto.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCategoryInput {

    @NotEmpty(message = "Name may not be empty")
    @NotBlank(message = "Name may not be blank")
    @Size(min = 2, max = 50)
    private String name;
}
