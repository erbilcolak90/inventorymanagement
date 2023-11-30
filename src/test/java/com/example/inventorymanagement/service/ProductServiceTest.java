package com.example.inventorymanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.inventorymanagement.exception.CustomExceptions;
import com.example.inventorymanagement.model.dto.inputs.*;
import com.example.inventorymanagement.model.dto.payloads.DeleteProductPayload;
import com.example.inventorymanagement.model.entities.Category;
import com.example.inventorymanagement.model.entities.Product;
import com.example.inventorymanagement.model.entities.Store;
import com.example.inventorymanagement.model.enums.Actions;
import com.example.inventorymanagement.model.enums.SortBy;
import com.example.inventorymanagement.repository.CategoryRepository;
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.repository.StoreRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepositoryMock;
    @Mock
    private StoreRepository storeRepositoryMock;
    @Mock
    private CategoryRepository categoryRepositoryMock;
    @Mock
    private LogService logServiceMock;
    @InjectMocks
    private ProductService productService;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product(1, "testproduct", 1, 100, 10);
    }

    @DisplayName("getProductById should return a valid Product for the given input")
    @Test
    public void testGetProductById_success() {
        GetProductByIdInput getProductByIdInput = new GetProductByIdInput(1);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(getProductByIdInput.getId())).thenReturn(Optional.ofNullable(testProduct));

        Product result = productService.getProductById(getProductByIdInput);

        assertNotNull(result);

    }

    @DisplayName("getProductById should fail with productNotFoundException message when product is deleted or not found")
    @Test
    public void testGetProductById_productNotFoundException() {
        GetProductByIdInput getProductByIdInput = new GetProductByIdInput(1);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(getProductByIdInput.getId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                productService.getProductById(getProductByIdInput));

        assertEquals("Product is not found", exception.getMessage());

    }

    @DisplayName("getProductsByStoreAttribute should succeed and return a list of stores with filtered products")
    @Test
    public void testGetProductsByStoreAttribute_success() {
        int productId = 1;
        String filterField = "name";
        GetProductByStoreAttributeInput input = new GetProductByStoreAttributeInput(productId, filterField);

        Product testProduct = new Product();
        testProduct.setId(productId);

        Store store1 = new Store();
        store1.setId(1);
        store1.setName("Store 1");
        store1.setProducts(Collections.singletonMap(productId, 10));

        Store store2 = new Store();
        store2.setId(2);
        store2.setName("Store 2");
        store2.setProducts(Collections.singletonMap(productId, 5));

        List<Integer> storeIds = Arrays.asList(store1.getId(), store2.getId());

        when(productRepositoryMock.findByIdAndIsDeletedFalse(productId)).thenReturn(Optional.of(testProduct));
        when(storeRepositoryMock.findStoreIdsByProductId(productId)).thenReturn(storeIds);
        when(storeRepositoryMock.findStoresByStoreIdsAndAttribute(storeIds, filterField)).thenReturn(Arrays.asList(store1, store2));

        //service method call
        List<Store> result = productService.getProductsByStoreAttribute(input);

        //comparing result with expected
        assertNotNull(result);
        assertEquals(2, result.size());

        Store resultStore1 = result.get(0);
        assertEquals(store1.getId(), resultStore1.getId());
        assertEquals(store1.getName(), resultStore1.getName());
        Map<Integer, Integer> expectedProducts1 = new HashMap<>(store1.getProducts());
        expectedProducts1.put(productId, store1.getProducts().get(productId));
        assertEquals(expectedProducts1, resultStore1.getProducts());

        Store resultStore2 = result.get(1);
        assertEquals(store2.getId(), resultStore2.getId());
        assertEquals(store2.getName(), resultStore2.getName());
        Map<Integer, Integer> expectedProducts2 = new HashMap<>(store2.getProducts());
        expectedProducts2.put(productId, store2.getProducts().get(productId));
        assertEquals(expectedProducts2, resultStore2.getProducts());

        verify(productRepositoryMock, times(1)).findByIdAndIsDeletedFalse(productId);
        verify(storeRepositoryMock, times(1)).findStoreIdsByProductId(productId);
        verify(storeRepositoryMock, times(1)).findStoresByStoreIdsAndAttribute(storeIds, filterField);
    }

    @DisplayName("getProductsByStoreAttribute should fail with productNotFoundException message")
    @Test
    public void testGetProductsByStoreAttribute_ProductNotFound() {

        when(productRepositoryMock.findByIdAndIsDeletedFalse(anyInt())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                productService.getProductsByStoreAttribute(new GetProductByStoreAttributeInput(1, "filterField")));

        assertEquals("Product is not found", exception.getMessage());
    }


    @DisplayName("createProducts should succeed and return the created Product")
    @Test
    public void testCreateProducts_success() {
        CreateProductInput createProductInput = new CreateProductInput("NewProduct", 1, 10, 5);
        when(productRepositoryMock.findByName("newproduct")).thenReturn(Optional.empty());
        when(productRepositoryMock.save(any())).thenReturn(new Product());

        Product createdProduct = productService.createProduct(createProductInput);

        assertNotNull(createdProduct);
        assertEquals("newproduct", createdProduct.getName());

        verify(logServiceMock, times(1)).logInfo(Actions.CREATE_PRODUCT.name() + " Product Id : " + createdProduct.getId());

    }

    @DisplayName("createProduct should fail with ProductNameIsAlreadyExistException message")
    @Test
    void testCreateProduct_ProductNameAlreadyExists() {
        CreateProductInput createProductInput = new CreateProductInput("ExistingProduct", 1, 10, 5);
        when(productRepositoryMock.findByName("existingproduct")).thenReturn(Optional.of(testProduct));

        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                productService.createProduct(createProductInput)
        );

        assertEquals("Product name is already exist", exception.getMessage());
    }

    @DisplayName("deleteProduct should soft delete an existing product and return DeleteProductPayload")
    @Test
    void testDeleteProduct_Success() {

        DeleteProductInput deleteProductInput = new DeleteProductInput(1);
        when(productRepositoryMock.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.of(testProduct));

        DeleteProductPayload result = productService.deleteProduct(deleteProductInput);

        assertTrue(result.isDeleted());
        assertEquals("The product was successfully deleted.", result.getMessage());
        verify(logServiceMock, times(1)).logInfo(Actions.DELETE_PRODUCT.name() + "  Product Id : " + testProduct.getId());
    }

    @DisplayName("deleteProduct should return false if the product is already deleted or does not exist")
    @Test
    void testDeleteProduct_ProductAlreadyDeleted() {
        DeleteProductInput deleteProductInput = new DeleteProductInput(1);
        when(productRepositoryMock.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.empty());

        DeleteProductPayload result = productService.deleteProduct(deleteProductInput);

        assertFalse(result.isDeleted());
        assertEquals("The product has already been deleted or this product has never been", result.getMessage());
    }

    @DisplayName("updateProduct should update an existing product successfully")
    @Test
    void testUpdateProduct_Success() {

        UpdateProductInput updateProductInput = new UpdateProductInput();
        updateProductInput.setId(1);
        updateProductInput.setName("NewProductName");
        updateProductInput.setCategoryId(2);
        updateProductInput.setQuantity(10);
        updateProductInput.setCriticalLevel(5);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.of(testProduct));
        when(productRepositoryMock.findByName("NewProductName".toLowerCase())).thenReturn(Optional.empty());

        Product updatedProduct = productService.updateProduct(updateProductInput);

        assertEquals("newproductname", updatedProduct.getName());
        assertEquals(2, updatedProduct.getCategoryId());
        assertEquals(110, updatedProduct.getQuantity()); // (5 + 10)
        assertEquals(5, updatedProduct.getCriticalLevel());

        verify(productRepositoryMock).save(any());
        verify(logServiceMock).logInfo(Actions.UPDATE_PRODUCT.name() + " Product Id : " + updatedProduct.getId());

    }

    @DisplayName("updateProduct should throw ProductNotFoundException for a not existing product")
    @Test
    void testUpdateProduct_ProductNotFound() {

        UpdateProductInput updateProductInput = new UpdateProductInput();
        updateProductInput.setId(1);
        when(productRepositoryMock.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class,
                () -> productService.updateProduct(updateProductInput));

        assertEquals("Product is not found", exception.getMessage());

    }

    @DisplayName("updateProduct should throw ProductNameIsAlreadExist for existing product-name")
    @Test
    public void testUpdateProduct_ProductNameIsAlreadyExist() {
        UpdateProductInput updateProductInput = new UpdateProductInput();
        updateProductInput.setId(1);
        updateProductInput.setName("NewProductName");
        updateProductInput.setCategoryId(2);
        updateProductInput.setQuantity(10);
        updateProductInput.setCriticalLevel(5);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.of(testProduct));
        when(productRepositoryMock.findByName("NewProductName".toLowerCase())).thenReturn(Optional.of(testProduct));

        CustomExceptions exception = assertThrows(CustomExceptions.class, () -> productService.updateProduct(updateProductInput));

        assertEquals("Product name is already exist", exception.getMessage());
    }

    @DisplayName("getProductsByCategory should return products for a valid category")
    @Test
    void testGetProductsByCategory_Success() {
        int categoryId = 1;
        GetProductsByCategoryInput input = new GetProductsByCategoryInput(categoryId, new PaginationInput(1, 10, "name", SortBy.ASC));

        Category testCategory = new Category();
        testCategory.setId(categoryId);
        List<Product> testProducts = Arrays.asList(
                new Product(1, "Product1", 1, 50, 5),
                new Product(2, "Product2", 1, 30, 3)
        );

        when(categoryRepositoryMock.findById(categoryId)).thenReturn(Optional.of(testCategory));

        when(productRepositoryMock.findByCategoryId(eq(testCategory.getId()), any(Pageable.class))).thenReturn(new PageImpl<>(testProducts));

        Page<Product> result = productService.getProductsByCategory(input);

        assertNotNull(result);
        assertEquals(testProducts.size(), result.getContent().size());

    }

    @DisplayName("getProductsByCategory should throw CategoryNotFoundException for an invalid category")
    @Test
    void testGetProductsByCategory_CategoryNotFound() {
        int invalidCategoryId = 999; // categoryId doesn't exist
        GetProductsByCategoryInput input = new GetProductsByCategoryInput(invalidCategoryId, new PaginationInput(1, 10, "name", SortBy.ASC));

        when(categoryRepositoryMock.findById(invalidCategoryId)).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () -> productService.getProductsByCategory(input));

        assertEquals("Category not found", exception.getMessage());
    }



    @AfterEach
    void tearDown() {
        testProduct = null;
    }


}
