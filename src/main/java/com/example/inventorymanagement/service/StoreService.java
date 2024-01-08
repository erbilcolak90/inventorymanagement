package com.example.inventorymanagement.service;

import com.example.inventorymanagement.model.entities.Product;
import com.example.inventorymanagement.model.entities.Store;
import com.example.inventorymanagement.exception.CustomExceptions;
import com.example.inventorymanagement.model.dto.inputs.AddProductStoreInput;
import com.example.inventorymanagement.model.dto.inputs.CreateStoreInput;
import com.example.inventorymanagement.model.dto.inputs.RemoveProductToStoreInput;
import com.example.inventorymanagement.model.entities.StoreProduct;
import com.example.inventorymanagement.model.enums.Actions;
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.repository.StoreProductRepository;
import com.example.inventorymanagement.repository.StoreRepository;
import com.example.inventorymanagement.service.eventservice.InventoryAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final InventoryAlertService inventoryAlertService;
    private final StoreProductRepository storeProductRepository;
    private final LogService logService;

    @Transactional
    public Store createStore(CreateStoreInput createStoreInput) {
        Store dbStore = storeRepository.findByName(createStoreInput.getName().toLowerCase());

        if (dbStore == null) {
            Store newStore = new Store();
            newStore.setName(createStoreInput.getName().toLowerCase());
            newStore.setAddress(createStoreInput.getAddress().toLowerCase());
            newStore.setCity(createStoreInput.getCity());
            newStore.setRegion(createStoreInput.getRegion());
            storeRepository.save(newStore);
            logService.logInfo(Actions.CREATE_STORE.name() + " " + " Store Id : " + newStore.getId());
            return newStore;

        } else {
            throw CustomExceptions.storeNameIsAlreadyExistException();

        }
    }

    @Transactional
    public StoreProduct addProductToStore(AddProductStoreInput addProductStoreInput) {
        int inputProductId = addProductStoreInput.getProductId();
        int inputStoreId = addProductStoreInput.getStoreId();
        int inputQuantity = addProductStoreInput.getQuantity();

        //Checking productId and storeId from database
        Product dbProduct = productRepository.findByIdAndIsDeletedFalse(inputProductId).orElseThrow(CustomExceptions::productNotFoundException);
        Store dbStore = storeRepository.findById(inputStoreId).orElseThrow(CustomExceptions::storeNotFoundException);

        //Check input-quantity with product quantity
        if (dbProduct.getQuantity() < inputQuantity) {
            throw CustomExceptions.productQuantityLessThanInputQuantityException();

        }

        StoreProduct existsProductInStore = storeProductRepository.findByStore_idAndProduct_id(dbStore.getId(), dbProduct.getId()).orElse(null);
        int oldQuantity = dbProduct.getQuantity();

        if (existsProductInStore != null) {

            existsProductInStore.setQuantity(existsProductInStore.getQuantity() + inputQuantity);
            storeProductRepository.save(existsProductInStore);

        }

        // if store contains productId. added to the existing quantity with input quantity
        else {

            existsProductInStore = new StoreProduct();
            existsProductInStore.setProduct_id(dbProduct.getId());
            existsProductInStore.setStore_id(dbStore.getId());
            existsProductInStore.setQuantity(inputQuantity);

            storeProductRepository.save(existsProductInStore);

        }

        // product quantity update after added process.

        dbProduct.setQuantity(oldQuantity - inputQuantity);

        dbProduct.setUpdateDate(new Date());
        productRepository.save(dbProduct);

        // InventoryAlert check quantity and critical level. if quantity less than critical level. alert triggered
        inventoryAlertService.handleInventoryUpdateEvent(dbProduct);

        dbStore.setUpdateDate(new Date());
        storeRepository.save(dbStore);

        logService.logInfo(Actions.ADD_PRODUCT_TO_STORE.name() + " " + addProductStoreInput.getQuantity() + " pieces of the product Id : " + dbProduct.getId() + " have been added to store Id : " + dbStore.getId());

        return existsProductInStore;
    }


    @Transactional
    public StoreProduct removeProductToStore(RemoveProductToStoreInput removeProductToStoreInput) {
        Product dbProduct = productRepository.findByIdAndIsDeletedFalse(removeProductToStoreInput.getProductId()).orElseThrow(CustomExceptions::productNotFoundException);
        Store dbStore = storeRepository.findById(removeProductToStoreInput.getStoreId()).orElseThrow(CustomExceptions::storeNotFoundException);
        StoreProduct existsProductInStore = storeProductRepository.findByStore_idAndProduct_id(dbStore.getId(), dbProduct.getId()).orElse(null);

        if (existsProductInStore != null) {

            if (existsProductInStore.getQuantity() >= removeProductToStoreInput.getQuantity()) {
                existsProductInStore.setQuantity(existsProductInStore.getQuantity() - removeProductToStoreInput.getQuantity());

                storeProductRepository.save(existsProductInStore);

                dbStore.setUpdateDate(new Date());
                storeRepository.save(dbStore);

                dbProduct.setQuantity(dbProduct.getQuantity() + removeProductToStoreInput.getQuantity());
                dbProduct.setUpdateDate(new Date());
                productRepository.save(dbProduct);

                logService.logInfo(Actions.REMOVE_PRODUCT_TO_STORE.name() + " " + removeProductToStoreInput.getQuantity() + " pieces of the product Id : " + dbProduct.getId() + " have been removed to store Id : " + dbStore.getId());

                return existsProductInStore;

            } else {
                throw CustomExceptions.productQuantityAtStoreLessThanInputQuantityException();
            }
        } else {
            throw CustomExceptions.productNotInTheStoreException();
        }

    }
}
