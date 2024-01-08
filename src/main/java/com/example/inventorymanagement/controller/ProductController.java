package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.model.dto.StoreProductDTO;
import com.example.inventorymanagement.model.dto.inputs.*;
import com.example.inventorymanagement.model.entities.Product;
import com.example.inventorymanagement.model.dto.payloads.DeleteProductPayload;
import com.example.inventorymanagement.model.entities.StoreProduct;
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

    @PostMapping("/getProductByRegion")
    public ResponseEntity<List<StoreProductDTO>> getProductByRegion(@RequestBody GetProductsByRegionInput getProductsByRegionInput){
        return ResponseEntity.ok(productService.getProductByRegion(getProductsByRegionInput));
    }

    @PostMapping("/getProductByCity")
    public ResponseEntity<List<StoreProductDTO>> getProductByCity(@RequestBody GetProductsByCityInput getProductsByCityInput){
        return ResponseEntity.ok(productService.getProductByCity(getProductsByCityInput));
    }

    @PostMapping("/getProductsByCategory")
    public ResponseEntity<Page<Product>> getProductsByCategory(@RequestBody GetProductsByCategoryInput getProductsByCategoryInput){
        return ResponseEntity.ok(productService.getProductsByCategory(getProductsByCategoryInput));
    }

    @PostMapping("/getProductInStore")
    public ResponseEntity<StoreProduct> getProductInStore(@RequestBody GetProductInStoreInput getProductInStoreInput){
        return ResponseEntity.ok(productService.getProductInStore(getProductInStoreInput));
    }
}
