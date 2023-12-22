package com.example.inventorymanagement.service;

import com.example.inventorymanagement.model.entities.Product;
import com.example.inventorymanagement.model.entities.Store;
import com.example.inventorymanagement.exception.CustomExceptions;
import com.example.inventorymanagement.model.dto.inputs.AddProductStoreInput;
import com.example.inventorymanagement.model.dto.inputs.CreateStoreInput;
import com.example.inventorymanagement.model.dto.inputs.RemoveProductToStoreInput;
import com.example.inventorymanagement.model.enums.Actions;
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.repository.StoreProductsRepository;
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
    private final StoreProductsRepository storeProductsRepository;
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
    public Store addProductToStore(AddProductStoreInput addProductStoreInput) {
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

        boolean existsProductInStore = storeRepository.existsProductInStore(dbStore.getId(), dbProduct.getId());

        if (existsProductInStore) {
            storeProductsRepository.updateStoreProductQuantity(dbStore.getId(), dbProduct.getId(), inputQuantity);
        }

        // if store contains productId. added to the existing quantity with input quantity
        else {
            dbStore.getProducts().add(dbProduct);
        }

        // product quantity update after added process.
        int oldQuantity = dbProduct.getQuantity();
        dbProduct.setQuantity(oldQuantity - inputQuantity);

        dbProduct.setUpdateDate(new Date());
        productRepository.save(dbProduct);

        // InventoryAlert check quantity and critical level. if quantity less than critical level. alert triggered
        inventoryAlertService.handleInventoryUpdateEvent(dbProduct);

        dbStore.setUpdateDate(new Date());
        storeRepository.save(dbStore);

        logService.logInfo(Actions.ADD_PRODUCT_TO_STORE.name() + " " + addProductStoreInput.getQuantity() + " pieces of the product Id : " + dbProduct.getId() + " have been added to store Id : " + dbStore.getId());

        return dbStore;
    }


    @Transactional
    public Store removeProductToStore(RemoveProductToStoreInput removeProductToStoreInput) {
        Product dbProduct = productRepository.findByIdAndIsDeletedFalse(removeProductToStoreInput.getProductId()).orElseThrow(CustomExceptions::productNotFoundException);
        Store dbStore = storeRepository.findById(removeProductToStoreInput.getStoreId()).orElseThrow(CustomExceptions::storeNotFoundException);

        boolean existsProductInStore = storeRepository.existsProductInStore(dbProduct.getId(), dbStore.getId());

        if (existsProductInStore) {
            boolean result = storeProductsRepository.removeProductInStore(dbProduct.getId(), dbProduct.getId(), (removeProductToStoreInput.getQuantity()));

            if (result) {
                dbStore.setUpdateDate(new Date());
                storeRepository.save(dbStore);

                dbProduct.setQuantity(dbProduct.getQuantity() + removeProductToStoreInput.getQuantity());
                dbProduct.setUpdateDate(new Date());
                productRepository.save(dbProduct);

                logService.logInfo(Actions.REMOVE_PRODUCT_TO_STORE.name() + " " + removeProductToStoreInput.getQuantity() + " pieces of the product Id : " + dbProduct.getId() + " have been removed to store Id : " + dbStore.getId());
            } else {
                throw CustomExceptions.productQuantityAtStoreLessThanInputQuantityException();
            }

            return dbStore;
        } else {
            throw CustomExceptions.productNotInTheStoreException();
        }

    }
}
