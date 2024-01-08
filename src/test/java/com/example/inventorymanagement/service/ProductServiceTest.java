package com.example.inventorymanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.inventorymanagement.exception.CustomExceptions;
import com.example.inventorymanagement.model.dto.StoreProductDTO;
import com.example.inventorymanagement.model.dto.inputs.*;
import com.example.inventorymanagement.model.dto.payloads.DeleteProductPayload;
import com.example.inventorymanagement.model.entities.Category;
import com.example.inventorymanagement.model.entities.Product;
import com.example.inventorymanagement.model.entities.Store;
import com.example.inventorymanagement.model.entities.StoreProduct;
import com.example.inventorymanagement.model.enums.Actions;
import com.example.inventorymanagement.model.enums.Cities;
import com.example.inventorymanagement.model.enums.Regions;
import com.example.inventorymanagement.model.enums.SortBy;
import com.example.inventorymanagement.repository.CategoryRepository;
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.repository.StoreProductRepository;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepositoryMock;
    @Mock
    private StoreRepository storeRepositoryMock;
    @Mock
    private CategoryRepository categoryRepositoryMock;
    @Mock
    private StoreProductRepository storeProductRepositoryMock;
    @Mock
    private LogService logServiceMock;
    @InjectMocks
    private ProductService productService;
    private Product testProduct;
    private Store testStore;

    private StoreProduct testStoreProduct;

    private StoreProductDTO testStoreProductDTO;

    List<StoreProductDTO> storeProductDTOList = new ArrayList<>();
    List<Integer> storeIdList = new ArrayList<>();

    List<StoreProduct> storeProductList = new ArrayList<>();

    List<Store> storeList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        testStore = new Store();
        Date testDate = new Date();
        testStore.setId(1);
        testStore.setName("teststorename");
        testStore.setAddress("teststoreaddress");
        testStore.setRegion(Regions.AKDENIZ);
        testStore.setCity(Cities.ADANA);
        testStore.setCreateDate(testDate);
        testStore.setUpdateDate(testDate);
        testStore.setDeleted(false);
        testProduct = new Product(1, "testproduct", 1, 100, 10);
        testStoreProductDTO = new StoreProductDTO();
        testStoreProductDTO.setId(1);
        testStoreProductDTO.setName("testStoreProductName");
        testStoreProductDTO.setAddress("testadress");
        testStoreProductDTO.setRegion(Regions.AKDENIZ);
        testStoreProductDTO.setCity(Cities.ADANA);
        testStoreProductDTO.setQuantity(100);
        testStoreProductDTO.setProduct_id(1);

        testStoreProduct = new StoreProduct();
        testStoreProduct.setId(1);
        testStoreProduct.setStore_id(1);
        testStoreProduct.setProduct_id(1);
        testStoreProduct.setQuantity(100);

        storeProductDTOList.add(testStoreProductDTO);
        storeIdList.add(1);
        storeProductList.add(testStoreProduct);
        storeList.add(testStore);
    }

    @DisplayName("getProductById should return a valid Product for the given input")
    @Test
    void testGetProductById_success() {
        GetProductByIdInput getProductByIdInput = new GetProductByIdInput(1);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(getProductByIdInput.getId())).thenReturn(Optional.ofNullable(testProduct));

        Product result = productService.getProductById(getProductByIdInput);

        assertNotNull(result);

    }

    @DisplayName("getProductById should fail with productNotFoundException message when product is deleted or not found")
    @Test
    void testGetProductById_productNotFoundException() {
        GetProductByIdInput getProductByIdInput = new GetProductByIdInput(1);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(getProductByIdInput.getId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                productService.getProductById(getProductByIdInput));

        assertEquals("Product is not found", exception.getMessage());

    }

    @DisplayName("getProductsByRegion should succeed and return the StoreProductDTO list")
    @Test
    void testGetProductsByRegion_success() {
        GetProductsByRegionInput getProductsByRegionInput = new GetProductsByRegionInput(Regions.AKDENIZ, 1);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(getProductsByRegionInput.getProductId())).thenReturn(Optional.of(testProduct));
        when(storeRepositoryMock.findByRegion(getProductsByRegionInput.getRegion())).thenReturn(Arrays.asList(1)); // Store IDs
        when(storeProductRepositoryMock.findAllByIdIn(Arrays.asList(1), getProductsByRegionInput.getProductId())).thenReturn(Arrays.asList(testStoreProduct));
        when(storeRepositoryMock.findAllByIdIn(Arrays.asList(1))).thenReturn(Arrays.asList(testStore));
        List<StoreProductDTO> result = productService.getProductByRegion(getProductsByRegionInput);

        assertEquals(storeProductDTOList.get(0).getProduct_id(), result.get(0).getProduct_id());

    }

    @DisplayName("getProductsByRegion should failed and return ProductNotFound exception")
    @Test
    void testGetProductsByRegion_failed_productNotFoundException() {
        GetProductsByRegionInput getProductsByRegionInput = new GetProductsByRegionInput(Regions.AKDENIZ, 1);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(getProductsByRegionInput.getProductId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class,
                () -> productService.getProductByRegion(getProductsByRegionInput));

        assertEquals("Product is not found", exception.getMessage());

    }

    @DisplayName("getProductsByCity should succeed and return the StoreProductDTO list")
    @Test
    void testGetProductsByCity_success() {
        GetProductsByCityInput getProductsByCityInput = new GetProductsByCityInput(Cities.ADANA, 1);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(getProductsByCityInput.getProductId())).thenReturn(Optional.of(testProduct));
        when(storeRepositoryMock.findByCity(getProductsByCityInput.getCity())).thenReturn(Arrays.asList(1)); // Store IDs
        when(storeProductRepositoryMock.findAllByIdIn(Arrays.asList(1), getProductsByCityInput.getProductId())).thenReturn(Arrays.asList(testStoreProduct));
        when(storeRepositoryMock.findAllByIdIn(Arrays.asList(1))).thenReturn(Arrays.asList(testStore));
        List<StoreProductDTO> result = productService.getProductByCity(getProductsByCityInput);

        assertEquals(storeProductDTOList.get(0).getProduct_id(), result.get(0).getProduct_id());

    }

     @DisplayName("getProductsByCity should failed and return ProductNotFound exception")
     @Test
     void testGetProductsByCity_failed_productNotFoundException() {
         GetProductsByCityInput getProductsByCityInput = new GetProductsByCityInput(Cities.ADANA, 1);

         when(productRepositoryMock.findByIdAndIsDeletedFalse(getProductsByCityInput.getProductId())).thenReturn(Optional.empty());

         CustomExceptions exception = assertThrows(CustomExceptions.class,
                 () -> productService.getProductByCity(getProductsByCityInput));

         assertEquals("Product is not found", exception.getMessage());

     }
    @DisplayName("getProductInStore should success and return StoreProduct")
    @Test
    void testGetProductInStore_success() {
        GetProductInStoreInput getProductInStoreInput = new GetProductInStoreInput(1, 1);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(getProductInStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(getProductInStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));
        when(storeProductRepositoryMock.findByStore_idAndProduct_id(testStore.getId(), testProduct.getId())).thenReturn(Optional.ofNullable(testStoreProduct));

        StoreProduct result = productService.getProductInStore(getProductInStoreInput);

        assertEquals(testStoreProduct.getQuantity(), result.getQuantity());
    }

    @DisplayName("getProductInStore should failed and throw ProductNotFoundException")
    @Test
    void testGetProductInStore_failed_productNotFoundException() {
        GetProductInStoreInput getProductInStoreInput = new GetProductInStoreInput(1, 1);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(getProductInStoreInput.getProductId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class,
                () -> productService.getProductInStore(getProductInStoreInput));

        assertEquals("Product is not found", exception.getMessage());
    }

    @DisplayName("getProductInStore should failed and throw StoreNotFoundException")
    @Test
    void testGetProductInStore_failed_storeNotFoundException() {
        GetProductInStoreInput getProductInStoreInput = new GetProductInStoreInput(1, 1);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(getProductInStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(getProductInStoreInput.getStoreId())).thenReturn(Optional.empty());
        CustomExceptions exception = assertThrows(CustomExceptions.class,
                () -> productService.getProductInStore(getProductInStoreInput));

        assertEquals("Store not found", exception.getMessage());
    }

    @DisplayName("getProductInStore should failed and throw productNotInStoreException")
    @Test
    void testGetProductInStore_failed_productNotInStoreException() {
        GetProductInStoreInput getProductInStoreInput = new GetProductInStoreInput(1, 1);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(getProductInStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(getProductInStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));
        when(storeProductRepositoryMock.findByStore_idAndProduct_id(testStore.getId(), testProduct.getId())).thenReturn(Optional.empty());
        CustomExceptions exception = assertThrows(CustomExceptions.class,
                () -> productService.getProductInStore(getProductInStoreInput));

        assertEquals("The product is not in the store", exception.getMessage());
    }


    @DisplayName("createProducts should succeed and return the created Product")
    @Test
    void testCreateProducts_success() {
        CreateProductInput createProductInput = new CreateProductInput("testproduct", 1, 10, 5);
        Category category = new Category(1, "testcategory", null);
        when(productRepositoryMock.findByName("testproduct")).thenReturn(Optional.empty());
        when(categoryRepositoryMock.findById(createProductInput.getCategoryId())).thenReturn(Optional.of(category));
        when(productRepositoryMock.save(any())).thenReturn(testProduct);

        Product createdProduct = productService.createProduct(createProductInput);

        assertNotNull(createdProduct);
        assertEquals("testproduct", createdProduct.getName());

        verify(logServiceMock, times(1)).logInfo(Actions.CREATE_PRODUCT.name() + " Product Id : " + createdProduct.getId());

    }

    @DisplayName("createProduct should failed with ProductNameIsAlreadyExistException message")
    @Test
    void testCreateProduct_failed_productNameAlreadyExists() {
        CreateProductInput createProductInput = new CreateProductInput("ExistingProduct", 1, 10, 5);
        Category category = new Category(1, "testcategory", null);
        when(productRepositoryMock.findByName("existingproduct")).thenReturn(Optional.ofNullable(testProduct));
        when(categoryRepositoryMock.findById(createProductInput.getCategoryId())).thenReturn(Optional.of(category));

        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                productService.createProduct(createProductInput)
        );

        assertEquals("Product name is already exist", exception.getMessage());
    }

    @DisplayName("createProduct should failed with CategoryNotFoundException")
    @Test
    void testCreateProduct_failed_categoryNotFoundException() {
        CreateProductInput createProductInput = new CreateProductInput("ExistingProduct", 1, 10, 5);
        when(productRepositoryMock.findByName("existingproduct")).thenReturn(Optional.empty());
        when(categoryRepositoryMock.findById(createProductInput.getCategoryId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                productService.createProduct(createProductInput)
        );

        assertEquals("Category not found", exception.getMessage());
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
    void testDeleteProduct_ProductAlreadyDeletedException() {
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
        Category category = new Category(2, "testcategory", null);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.of(testProduct));
        when(productRepositoryMock.findByName("NewProductName".toLowerCase())).thenReturn(Optional.empty());
        when(categoryRepositoryMock.findById(updateProductInput.getCategoryId())).thenReturn(Optional.of(category));

        Product updatedProduct = productService.updateProduct(updateProductInput);

        assertEquals("newproductname", updatedProduct.getName());
        assertEquals(2, updatedProduct.getCategoryId());
        assertEquals(110, updatedProduct.getQuantity()); // (5 + 10)
        assertEquals(5, updatedProduct.getCriticalLevel());

        verify(productRepositoryMock).save(any());
        verify(logServiceMock).logInfo(Actions.UPDATE_PRODUCT.name() + " Product Id : " + updatedProduct.getId());

    }

    @DisplayName("updateProduct should failed and throw ProductNotFoundException for a not existing product")
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
    void testUpdateProduct_ProductNameIsAlreadyExist() {
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

    @DisplayName("updateProduct should failed and throw categoryNotFoundException")
    @Test
    void testUpdateProduct_categoryNotFoundException() {
        UpdateProductInput updateProductInput = new UpdateProductInput();
        updateProductInput.setId(1);
        updateProductInput.setName("NewProductName");
        updateProductInput.setCategoryId(2);
        updateProductInput.setQuantity(10);
        updateProductInput.setCriticalLevel(5);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.of(testProduct));
        when(productRepositoryMock.findByName("NewProductName".toLowerCase())).thenReturn(Optional.empty());
        when(categoryRepositoryMock.findById(updateProductInput.getCategoryId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () -> productService.updateProduct(updateProductInput));

        assertEquals("Category not found", exception.getMessage());
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
        testStore = null;
        testStoreProduct = null;
        testStoreProductDTO = null;
        storeProductDTOList = null;
        storeIdList = null;
        storeProductList = null;
        storeList = null;
    }
}
