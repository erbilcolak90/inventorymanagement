package com.example.inventorymanagement.model.dto.inputs;

import com.example.inventorymanagement.model.enums.SortBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginationInput {

    private int page;
    private int size;
    private String fieldName;
    private SortBy sortBy;
}
