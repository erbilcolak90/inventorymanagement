package com.example.inventorymanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.inventorymanagement.exception.CustomExceptions;
import com.example.inventorymanagement.model.dto.inputs.CreateCategoryInput;
import com.example.inventorymanagement.model.entities.Category;
import com.example.inventorymanagement.model.enums.Actions;
import com.example.inventorymanagement.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepositoryMock;
    @Mock
    private LogService logService;
    @InjectMocks
    private CategoryService categoryService;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1);
        //name must be lowercase
        testCategory.setName("existingcategory");
    }

    @DisplayName("createCategory should be success and return Category")
    @Test
    void testCreateCategory_success() {
        //Creation of variables
        CreateCategoryInput createCategoryInput = new CreateCategoryInput("existingcategory");

        when(categoryRepositoryMock.findByName("existingcategory")).thenReturn(null);
        when(categoryRepositoryMock.save(any())).thenReturn(testCategory);

        Category createdCategory = categoryService.createCategory(createCategoryInput);

        // Verify
        assertNotNull(createdCategory);
        assertEquals("existingcategory", createdCategory.getName());

        // LogService method should be called once with the specified message
        verify(logService, times(1)).logInfo(Actions.CREATE_CATEGORY.name() + " Category Id : " + createdCategory.getId());
    }

    @DisplayName("createTestCategory should be failed with Category Already Exists Exception message")
    @Test
    void testCreateCategory_CategoryAlreadyExistsException() {
        //creation of variables
        CreateCategoryInput createCategoryInput = new CreateCategoryInput("existingcategory");

        when(categoryRepositoryMock.findByName("existingcategory")).thenReturn(testCategory);

        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                categoryService.createCategory(createCategoryInput));

        assertEquals("Category name is already exist", exception.getMessage());

    }

    @AfterEach
    void tearDown() {
        testCategory = null;
    }
}