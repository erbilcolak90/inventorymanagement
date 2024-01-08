package com.example.inventorymanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.inventorymanagement.exception.CustomExceptions;
import com.example.inventorymanagement.model.dto.inputs.AddProductStoreInput;
import com.example.inventorymanagement.model.dto.inputs.CreateStoreInput;
import com.example.inventorymanagement.model.dto.inputs.RemoveProductToStoreInput;
import com.example.inventorymanagement.model.entities.Product;
import com.example.inventorymanagement.model.entities.Store;
import com.example.inventorymanagement.model.entities.StoreProduct;
import com.example.inventorymanagement.model.enums.Actions;
import com.example.inventorymanagement.model.enums.Cities;
import com.example.inventorymanagement.model.enums.Regions;
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.repository.StoreProductRepository;
import com.example.inventorymanagement.repository.StoreRepository;
import com.example.inventorymanagement.service.eventservice.InventoryAlertService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepositoryMock;
    @Mock
    private ProductRepository productRepositoryMock;
    @Mock
    private InventoryAlertService inventoryAlertService;
    @Mock
    private StoreProductRepository storeProductRepositoryMock;
    @Mock
    private LogService logService;
    @InjectMocks
    private StoreService storeService;

    private Store testStore;

    private Product testProduct;

    private StoreProduct testStoreProduct;

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

        testStoreProduct = new StoreProduct();
        testStoreProduct.setId(1);
        testStoreProduct.setStore_id(1);
        testStoreProduct.setProduct_id(1);
        testStoreProduct.setQuantity(100);


    }

    @DisplayName("createStore should be success and return created store")
    @Test
    void testCreateStore_success() {
        //Creation of variables
        CreateStoreInput createStoreInput = new CreateStoreInput("testStoreName", "testStoreAddress", Regions.AKDENIZ, Cities.ADANA);

        when(storeRepositoryMock.findByName("teststorename")).thenReturn(null);
        when(storeRepositoryMock.save(any())).thenReturn(testStore);

        Store createdStore = storeService.createStore(createStoreInput);

        //Verify
        assertNotNull(createdStore);
        assertEquals("teststorename", createdStore.getName());

        verify(logService, times(1)).logInfo(Actions.CREATE_STORE.name() + " " + " Store Id : " + createdStore.getId());

    }


    @DisplayName("createStore should be failed with StoreNameIsAlreadyExistException message")
    @Test
    void testCreateStore_storeNameIsAlreadyExistException() {
        CreateStoreInput createStoreInput = new CreateStoreInput("testStoreName", "testStoreAddress", Regions.AKDENIZ, Cities.ADANA);

        when(storeRepositoryMock.findByName("teststorename")).thenReturn(testStore);

        CustomExceptions exception = assertThrows(CustomExceptions.class,
                () -> storeService.createStore(createStoreInput));

        assertEquals("Store name is already exist", exception.getMessage());

        verify(logService, never()).logInfo(anyString());
    }

    @DisplayName("addProductToStore should be return StoreProducts success and if product exist in store")
    @Test
    void testAddProductToStore_success_productExistInStore() {
        AddProductStoreInput addProductStoreInput = new AddProductStoreInput(1, 1, 10);
        Product dbProduct = new Product(1, "testproduct", 1, 90, 10);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(addProductStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(addProductStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));
        when(storeProductRepositoryMock.findByStore_idAndProduct_id(testStore.getId(), testProduct.getId())).thenReturn(Optional.of(testStoreProduct));

        testStoreProduct.setQuantity(testStoreProduct.getQuantity() + addProductStoreInput.getQuantity());

        when(storeProductRepositoryMock.save(any())).thenReturn(testStoreProduct);

        when(storeRepositoryMock.save(any())).thenReturn(testStore);
        when(productRepositoryMock.save(any())).thenReturn(testProduct);


        StoreProduct result = storeService.addProductToStore(addProductStoreInput);

        assertEquals(testStoreProduct.getQuantity(), result.getQuantity());
        assertEquals(testProduct.getQuantity(), dbProduct.getQuantity());

        verify(inventoryAlertService, times(1)).handleInventoryUpdateEvent(testProduct);
        verify(logService, times(1)).logInfo(Actions.ADD_PRODUCT_TO_STORE.name() + " " + addProductStoreInput.getQuantity() + " pieces of the product Id : " + testProduct.getId() + " have been added to store Id : " + testStore.getId());

    }

    @DisplayName("addProductToStore should be return StoreProducts success and if product does not exist in store")
    @Test
    void testAddProductToStore_success_productDoesNotExistInStore() {
        AddProductStoreInput addProductStoreInput = new AddProductStoreInput(1, 1, 10);
        Product dbProduct = new Product(1, "testproduct", 1, 90, 10);
        StoreProduct newStoreProduct = new StoreProduct(1, 1, 1, addProductStoreInput.getQuantity());

        when(productRepositoryMock.findByIdAndIsDeletedFalse(addProductStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(addProductStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));
        when(storeProductRepositoryMock.findByStore_idAndProduct_id(testStore.getId(), testProduct.getId())).thenReturn(Optional.empty());
        when(storeProductRepositoryMock.save(any())).thenReturn(newStoreProduct);
        when(storeRepositoryMock.save(any())).thenReturn(testStore);
        when(productRepositoryMock.save(any())).thenReturn(dbProduct);

        StoreProduct result = storeService.addProductToStore(addProductStoreInput);

        assertEquals(newStoreProduct.getQuantity(), result.getQuantity());

        verify(inventoryAlertService, times(1)).handleInventoryUpdateEvent(testProduct);
        verify(logService, times(1)).logInfo(Actions.ADD_PRODUCT_TO_STORE.name() + " " + addProductStoreInput.getQuantity() + " pieces of the product Id : " + testProduct.getId() + " have been added to store Id : " + testStore.getId());

    }


    @DisplayName("addProductToStore should be failed with Product Not Found message")
    @Test
    void testAddProductToStore_failed_productNotFoundException() {
        AddProductStoreInput addProductStoreInput = new AddProductStoreInput(1, 1, 10);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(addProductStoreInput.getProductId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () -> storeService.addProductToStore(addProductStoreInput));

        assertEquals("Product is not found", exception.getMessage());

    }


    @DisplayName("addProductToStore should be failed with Store Not Found message")
    @Test
    void testAddProductToStore_StoreNotFoundException() {
        AddProductStoreInput addProductStoreInput = new AddProductStoreInput(1, 1, 10);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(addProductStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(addProductStoreInput.getStoreId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                storeService.addProductToStore(addProductStoreInput)
        );

        assertEquals("Store not found", exception.getMessage());

    }

    @DisplayName("addProductToStore should be failed with Product Quantity Less Than Input Quantity Exception message")
    @Test
    void testAddProductToStore_failed_productQuantityLessThanInputQuantityException() {
        AddProductStoreInput addProductStoreInput = new AddProductStoreInput(1, 1, 101);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(addProductStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(addProductStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));

        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                storeService.addProductToStore(addProductStoreInput)
        );

        assertEquals("Product quantity may greater than input quantity", exception.getMessage());
    }

    @DisplayName("removeProductToStore should be success and return Store")
    @Test
    void testRemoveProductToStore_success() {
        RemoveProductToStoreInput removeProductToStoreInput = new RemoveProductToStoreInput(1, 1, 10);


        when(productRepositoryMock.findByIdAndIsDeletedFalse(removeProductToStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(removeProductToStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));
        when(storeProductRepositoryMock.findByStore_idAndProduct_id(testStore.getId(), testProduct.getId())).thenReturn(Optional.ofNullable(testStoreProduct));

        when(storeRepositoryMock.save(any())).thenReturn(testStore);
        when(productRepositoryMock.save(any())).thenReturn(testProduct);

        StoreProduct result = storeService.removeProductToStore(removeProductToStoreInput);

        assertEquals(90, result.getQuantity());
        assertEquals(110, testProduct.getQuantity());

        verify(logService, times(1)).logInfo(Actions.REMOVE_PRODUCT_TO_STORE.name() + " " + removeProductToStoreInput.getQuantity() + " pieces of the product Id : " + testProduct.getId() + " have been removed to store Id : " + testStore.getId());
    }

    @DisplayName("removeProductToStore should be failed with Product Not Found message")
    @Test
    void testRemoveProductToStore_failed_productNotFoundException() {
        RemoveProductToStoreInput removeProductToStoreInput = new RemoveProductToStoreInput(1, 1, 10);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(removeProductToStoreInput.getProductId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                storeService.removeProductToStore(removeProductToStoreInput)
        );

        assertEquals("Product is not found", exception.getMessage());

    }

    @DisplayName("removeProductToStore should be failed with Store Not Found message")
    @Test
    void testRemoveProductToStore_failed_StoreNotFoundException() {
        RemoveProductToStoreInput removeProductToStoreInput = new RemoveProductToStoreInput(1, 1, 10);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(removeProductToStoreInput.getProductId())).thenReturn(Optional.of(testProduct));
        when(storeRepositoryMock.findById(removeProductToStoreInput.getStoreId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                storeService.removeProductToStore(removeProductToStoreInput));

        assertEquals("Store not found", exception.getMessage());

    }


    @DisplayName("removeProductToStore should failed with Product Quantity At Store Less Than Input Quantity Exception message")
    @Test
    void testRemoveProductToStore_failed_productQuantityAtStoreLessThanInputQuantityException() {
        RemoveProductToStoreInput removeProductToStoreInput = new RemoveProductToStoreInput(1, 1, 200);


        when(productRepositoryMock.findByIdAndIsDeletedFalse(removeProductToStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(removeProductToStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));
        when(storeProductRepositoryMock.findByStore_idAndProduct_id(testStore.getId(), testProduct.getId())).thenReturn(Optional.ofNullable(testStoreProduct));


        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                storeService.removeProductToStore(removeProductToStoreInput));

        assertEquals("Quantity from Input may less or equal to product quantity at store", exception.getMessage());

    }


    @DisplayName("removeProductToStore should be failed with product Not In The Store Exception message")
    @Test
    void testRemoveProductToStore_failed_productNotInTheStoreException() {
        RemoveProductToStoreInput removeProductToStoreInput = new RemoveProductToStoreInput(1, 1, 0);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(removeProductToStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(removeProductToStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));
        when(storeProductRepositoryMock.findByStore_idAndProduct_id(testStore.getId(), testProduct.getId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () ->
                storeService.removeProductToStore(removeProductToStoreInput)
        );

        assertEquals("The product is not in the store", exception.getMessage());

    }

    @AfterEach
    void tearDown() {
        testStore = null;
        testProduct = null;
    }
}
