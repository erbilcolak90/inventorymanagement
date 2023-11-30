package com.example.inventorymanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.inventorymanagement.exception.CustomExceptions;
import com.example.inventorymanagement.model.dto.inputs.AddProductStoreInput;
import com.example.inventorymanagement.model.dto.inputs.CreateStoreInput;
import com.example.inventorymanagement.model.dto.inputs.RemoveProductToStoreInput;
import com.example.inventorymanagement.model.entities.Product;
import com.example.inventorymanagement.model.entities.Store;
import com.example.inventorymanagement.model.enums.Actions;
import com.example.inventorymanagement.model.enums.Cities;
import com.example.inventorymanagement.model.enums.Regions;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepositoryMock;
    @Mock
    private ProductRepository productRepositoryMock;
    @Mock
    private InventoryAlertService inventoryAlertService;
    @Mock
    private LogService logService;
    @InjectMocks
    private StoreService storeService;

    private Store testStore;

    private Product testProduct;

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

    }

    @DisplayName("createStore should be success and return created store")
    @Test
    public void testCreateStore_success() {
        //Creation of variables
        CreateStoreInput createStoreInput = new CreateStoreInput("testStoreName", "testStoreAddress", Regions.AKDENIZ, Cities.ADANA);

        when(storeRepositoryMock.findByName("teststorename")).thenReturn(null);
        when(storeRepositoryMock.save(any())).thenReturn(testStore);

        Store createdStore = storeService.createStore(createStoreInput);

        //Verify
        assertNotNull(createdStore);
        assertEquals("teststorename", createdStore.getName());
        assertNotNull(createdStore.getId());

        verify(logService, times(1)).logInfo(Actions.CREATE_STORE.name()+" "+" Store Id : " + createdStore.getId());

    }

    @DisplayName("createStore should be failed with StoreNameIsAlreadyExistException message")
    @Test
    public void testCreateStore_storeNameIsAlreadyExistException() {
        CreateStoreInput createStoreInput = new CreateStoreInput("testStoreName", "testStoreAddress", Regions.AKDENIZ, Cities.ADANA);

        when(storeRepositoryMock.findByName("teststorename")).thenReturn(testStore);

        CustomExceptions exception = assertThrows(CustomExceptions.class,
                () -> storeService.createStore(createStoreInput));

        assertEquals("Store name is already exist", exception.getMessage());

        verify(logService, never()).logInfo(anyString());
    }

    @DisplayName("addProductToStore should be success")
    @Test
    public void testAddProductToStore_success() {
        AddProductStoreInput addProductStoreInput = new AddProductStoreInput(1, 1, 10);
        Product savedProduct = new Product(1, "testproduct", 1, 90, 10);
        Map<Integer, Integer> products = new HashMap<>();
        testStore.setProducts(products);
        testStore.getProducts().put(1, 0);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(addProductStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(addProductStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));
        when(storeRepositoryMock.save(any())).thenReturn(testStore);
        when(productRepositoryMock.save(any())).thenReturn(savedProduct);

        Store result = storeService.addProductToStore(addProductStoreInput);

        assertEquals(10, result.getProducts().get(1));

        verify(inventoryAlertService, times(1)).handleInventoryUpdateEvent(savedProduct);
        verify(logService, times(1)).logInfo(Actions.ADD_PRODUCT_TO_STORE.name() +" "+ addProductStoreInput.getQuantity() + " pieces of the product Id : " + savedProduct.getId() + " have been added to store Id : " + testStore.getId());

    }

    @DisplayName("addProductToStore should be failed with Product Not Found message")
    @Test
    public void testAddProductToStore_productNotFoundException() {
        AddProductStoreInput addProductStoreInput = new AddProductStoreInput(1, 1, 10);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(addProductStoreInput.getProductId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () -> {
            storeService.addProductToStore(addProductStoreInput);
        });

        assertEquals("Product is not found", exception.getMessage());

    }

    @DisplayName("addProductToStore should be failed with Store Not Found message")
    @Test
    public void testAddProductToStore_StoreNotFoundException() {
        AddProductStoreInput addProductStoreInput = new AddProductStoreInput(1, 1, 10);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(addProductStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(addProductStoreInput.getStoreId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () -> {
            storeService.addProductToStore(addProductStoreInput);
        });

        assertEquals("Store not found", exception.getMessage());

    }

    @DisplayName("addProductToStore should be failed with Product Quantity Less Than Input Quantity Exception message")
    @Test
    public void testAddProductToStore_productQuantityLessThanInputQuantityException() {
        AddProductStoreInput addProductStoreInput = new AddProductStoreInput(1, 1, 101);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(addProductStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(addProductStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));

        CustomExceptions exception = assertThrows(CustomExceptions.class, () -> {
            storeService.addProductToStore(addProductStoreInput);
        });

        assertEquals("Product quantity may greater than input quantity", exception.getMessage());
    }

    @DisplayName("removeProductToStore should be success and return Store")
    @Test
    public void testRemoveProductToStore_success() {
        RemoveProductToStoreInput removeProductToStoreInput = new RemoveProductToStoreInput(1, 1, 10);
        Map<Integer, Integer> products = new HashMap<>();
        testStore.setProducts(products);
        testStore.getProducts().put(1, 100);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(removeProductToStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(removeProductToStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));
        when(storeRepositoryMock.save(any())).thenReturn(testStore);
        when(productRepositoryMock.save(any())).thenReturn(testProduct);

        Store result = storeService.removeProductToStore(removeProductToStoreInput);

        assertEquals(90, result.getProducts().get(1));
        assertEquals(110, testProduct.getQuantity());

        verify(logService, times(1)).logInfo(Actions.REMOVE_PRODUCT_TO_STORE.name()+" "+removeProductToStoreInput.getQuantity() + " pieces of the product Id : " + testProduct.getId() + " have been removed to store Id : " + testStore.getId());
    }

    @DisplayName("removeProductToStore should be failed with Product Not Found message")
    @Test
    public void testRemoveProductToStore_productNotFoundException() {
        RemoveProductToStoreInput removeProductToStoreInput = new RemoveProductToStoreInput(1, 1, 10);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(removeProductToStoreInput.getProductId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () -> {
            storeService.removeProductToStore(removeProductToStoreInput);
        });

        assertEquals("Product is not found", exception.getMessage());

    }

    @DisplayName("removeProductToStore should be failed with Store Not Found message")
    @Test
    public void testRemoveProductToStore_StoreNotFoundException() {
        RemoveProductToStoreInput removeProductToStoreInput = new RemoveProductToStoreInput(1, 1, 10);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(removeProductToStoreInput.getProductId())).thenReturn(Optional.of(testProduct));
        when(storeRepositoryMock.findById(removeProductToStoreInput.getStoreId())).thenReturn(Optional.empty());

        CustomExceptions exception = assertThrows(CustomExceptions.class, () -> {
            storeService.removeProductToStore(removeProductToStoreInput);
        });

        assertEquals("Store not found", exception.getMessage());

    }

    @DisplayName("removeProductToStore should fail with Product Quantity At Store Less Than Input Quantity Exception message")
    @Test
    public void testRemoveProductToStore_productQuantityAtStoreLessThanInputQuantityException() {
        RemoveProductToStoreInput removeProductToStoreInput = new RemoveProductToStoreInput(1, 1, 10);
        Map<Integer, Integer> products = new HashMap<>();
        testStore.setProducts(products);
        testStore.getProducts().put(1, 9);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(removeProductToStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(removeProductToStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));

        CustomExceptions exception = assertThrows(CustomExceptions.class, () -> {
            storeService.removeProductToStore(removeProductToStoreInput);
        });

        assertEquals("Quantity from Input may less or equal to product quantity at store", exception.getMessage());

    }

    @DisplayName("removeProductToStore should be failed with product Not In The Store Exception message")
    @Test
    public void testRemoveProductToStore_productNotInTheStoreException() {
        RemoveProductToStoreInput removeProductToStoreInput = new RemoveProductToStoreInput(1, 1, 0);
        Map<Integer, Integer> products = new HashMap<>();
        testStore.setProducts(products);
        testStore.getProducts().put(2, 10);

        when(productRepositoryMock.findByIdAndIsDeletedFalse(removeProductToStoreInput.getProductId())).thenReturn(Optional.ofNullable(testProduct));
        when(storeRepositoryMock.findById(removeProductToStoreInput.getStoreId())).thenReturn(Optional.ofNullable(testStore));

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
