package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.model.entities.Category;
import com.example.inventorymanagement.model.dto.inputs.CreateCategoryInput;
import com.example.inventorymanagement.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/createCategory")
    public ResponseEntity<Category> createCategory(@RequestBody CreateCategoryInput createCategoryInput){
        return ResponseEntity.ok(categoryService.createCategory(createCategoryInput));
    }
}
