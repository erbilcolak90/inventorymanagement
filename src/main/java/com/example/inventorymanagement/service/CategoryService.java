package com.example.inventorymanagement.service;

import com.example.inventorymanagement.model.entities.Category;
import com.example.inventorymanagement.exception.CustomExceptions;
import com.example.inventorymanagement.model.dto.inputs.CreateCategoryInput;
import com.example.inventorymanagement.model.enums.Actions;
import com.example.inventorymanagement.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService{

    private final CategoryRepository categoryRepository;
    private final LogService logService;

    @Transactional
    public Category createCategory(CreateCategoryInput createCategoryInput){
        Category dbCategory = categoryRepository.findByName(createCategoryInput.getName().toLowerCase());

        if(dbCategory == null){
            Category newCategory = new Category();
            newCategory.setName(createCategoryInput.getName().toLowerCase());
            categoryRepository.save(newCategory);

            logService.logInfo(Actions.CREATE_CATEGORY.name() + " Category Id : " + newCategory.getId());

            return newCategory;
        }else{
            throw CustomExceptions.categoryNameIsAlreadyExistException();
        }
    }


}
