package com.example.inventorymanagement.model.dto.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class DeleteProductPayload {

    private boolean isDeleted;
    private String message;
}
