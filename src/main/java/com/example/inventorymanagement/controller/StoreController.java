package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.model.entities.Store;
import com.example.inventorymanagement.model.dto.inputs.AddProductStoreInput;
import com.example.inventorymanagement.model.dto.inputs.CreateStoreInput;
import com.example.inventorymanagement.model.dto.inputs.RemoveProductToStoreInput;
import com.example.inventorymanagement.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/addProductToStore")
    public ResponseEntity<Store> addProductToStore(@RequestBody AddProductStoreInput addProductStoreInput) {
        return ResponseEntity.ok(storeService.addProductToStore(addProductStoreInput));
    }

    @PostMapping("/removeProductToStore")
    public ResponseEntity<Store> removeProductToStore(@RequestBody RemoveProductToStoreInput removeProductToStoreInput) {
        return ResponseEntity.ok(storeService.removeProductToStore(removeProductToStoreInput));
    }

    @PostMapping("/createStore")
    public ResponseEntity<Store> createStore(@RequestBody CreateStoreInput createStoreInput) {
        return ResponseEntity.ok(storeService.createStore(createStoreInput));
    }
}
