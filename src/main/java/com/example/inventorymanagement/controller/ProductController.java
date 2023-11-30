package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.model.dto.inputs.*;
import com.example.inventorymanagement.model.entities.Product;
import com.example.inventorymanagement.model.entities.Store;
import com.example.inventorymanagement.model.dto.payloads.DeleteProductPayload;
import com.example.inventorymanagement.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/getProductById")
    public ResponseEntity<Product> getProductById(@RequestBody GetProductByIdInput getProductByIdInput){
        return ResponseEntity.ok(productService.getProductById(getProductByIdInput));
    }

    @PostMapping("/createProduct")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductInput createProductInput){
        return ResponseEntity.ok(productService.createProduct(createProductInput));
    }

    @PostMapping("/deleteProduct")
    public ResponseEntity<DeleteProductPayload> deleteProduct(@RequestBody DeleteProductInput deleteProductInput){
        return ResponseEntity.ok(productService.deleteProduct(deleteProductInput));
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<Product> updateProduct(@RequestBody UpdateProductInput updateProductInput){
        return ResponseEntity.ok(productService.updateProduct(updateProductInput));
    }

    //Store name or store city or store region
    @PostMapping("/getProductByStoreAttribute")
    public ResponseEntity<List<Store>> getProductByStoreAttribute(@RequestBody GetProductByStoreAttributeInput getProductByStoreAttributeInput){
        return ResponseEntity.ok(productService.getProductsByStoreAttribute(getProductByStoreAttributeInput));
    }

    @PostMapping("/getProductsByCategory")
    public ResponseEntity<Page<Product>> getProductsByCategory(@RequestBody GetProductsByCategoryInput getProductsByCategoryInput){
        return ResponseEntity.ok(productService.getProductsByCategory(getProductsByCategoryInput));
    }
}
