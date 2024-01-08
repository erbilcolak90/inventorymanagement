package com.example.inventorymanagement.service;

import com.example.inventorymanagement.model.dto.StoreProductDTO;
import com.example.inventorymanagement.model.dto.inputs.*;
import com.example.inventorymanagement.model.entities.Category;
import com.example.inventorymanagement.model.entities.Product;
import com.example.inventorymanagement.model.entities.Store;
import com.example.inventorymanagement.model.entities.StoreProduct;
import com.example.inventorymanagement.model.enums.Actions;
import com.example.inventorymanagement.model.enums.Cities;
import com.example.inventorymanagement.model.enums.Regions;
import com.example.inventorymanagement.model.enums.SortBy;
import com.example.inventorymanagement.exception.CustomExceptions;
import com.example.inventorymanagement.model.dto.payloads.DeleteProductPayload;
import com.example.inventorymanagement.repository.CategoryRepository;
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.repository.StoreProductRepository;
import com.example.inventorymanagement.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final StoreProductRepository storeProductRepository;
    private final LogService logService;

    public Product getProductById(GetProductByIdInput getProductByIdInput) {

        return productRepository.findByIdAndIsDeletedFalse(getProductByIdInput.getId()).orElseThrow(CustomExceptions::productNotFoundException);

    }

    public List<StoreProductDTO> getProductByRegion(GetProductsByRegionInput getProductsByRegionInput) {

        Product dbProduct = productRepository.findByIdAndIsDeletedFalse(getProductsByRegionInput.getProductId()).orElseThrow(CustomExceptions::productNotFoundException);

        List<Integer> storeIds = storeRepository.findByRegion(getProductsByRegionInput.getRegion());

        return getProductByAttribute(storeIds, dbProduct.getId());
    }

    public List<StoreProductDTO> getProductByCity(GetProductsByCityInput getProductsByCityInput) {
        Product dbProduct = productRepository.findByIdAndIsDeletedFalse(getProductsByCityInput.getProductId()).orElseThrow(CustomExceptions::productNotFoundException);

        List<Integer> storeIds = storeRepository.findByCity(getProductsByCityInput.getCity());

        return getProductByAttribute(storeIds, dbProduct.getId());
    }

    public StoreProduct getProductInStore(GetProductInStoreInput getProductInStoreInput) {
        Product dbProduct = productRepository.findByIdAndIsDeletedFalse(getProductInStoreInput.getProductId()).orElseThrow(CustomExceptions::productNotFoundException);
        Store dbStore = storeRepository.findById(getProductInStoreInput.getStoreId()).orElseThrow(CustomExceptions::storeNotFoundException);

        StoreProduct dbStoreProduct = storeProductRepository.findByStore_idAndProduct_id(dbStore.getId(), dbProduct.getId()).orElse(null);
        if (dbStoreProduct != null) {
            return dbStoreProduct;
        } else {
            throw CustomExceptions.productNotInTheStoreException();
        }
    }

    @Transactional
    public Product createProduct(CreateProductInput createProductInput) throws CustomExceptions {
        Product dbProduct = productRepository.findByName(createProductInput.getName().toLowerCase()).orElse(null);
        Category dbCategory = categoryRepository.findById(createProductInput.getCategoryId()).orElseThrow(CustomExceptions::categoryNotFoundException);
        if (dbProduct == null) {
            Product newProduct = new Product();
            newProduct.setName(createProductInput.getName().toLowerCase());
            newProduct.setCategoryId(dbCategory.getId());
            newProduct.setQuantity(createProductInput.getQuantity());
            newProduct.setCriticalLevel(createProductInput.getCriticalLevel());
            productRepository.save(newProduct);

            logService.logInfo(Actions.CREATE_PRODUCT.name() + " Product Id : " + newProduct.getId());

            return newProduct;

        } else {
            throw CustomExceptions.productNameIsAlreadyExistException();
        }
    }

    @Transactional
    public DeleteProductPayload deleteProduct(DeleteProductInput deleteProductInput) {
        Product dbProduct = productRepository.findByIdAndIsDeletedFalse(deleteProductInput.getId()).orElse(null);
        if (dbProduct != null) {
            dbProduct.setDeleted(true);
            dbProduct.setUpdateDate(new Date());

            logService.logInfo(Actions.DELETE_PRODUCT.name() + "  Product Id : " + dbProduct.getId());

            productRepository.save(dbProduct);

            return new DeleteProductPayload(true, "The product was successfully deleted.");
        } else {

            return new DeleteProductPayload(false, "The product has already been deleted or this product has never been");
        }

    }

    @Transactional
    public Product updateProduct(UpdateProductInput updateProductInput) {

        Product dbProduct = productRepository.findByIdAndIsDeletedFalse(updateProductInput.getId()).orElseThrow(CustomExceptions::productNotFoundException);
        updateProduct(updateProductInput, dbProduct);
        dbProduct.setUpdateDate(new Date());
        productRepository.save(dbProduct);

        logService.logInfo(Actions.UPDATE_PRODUCT.name() + " Product Id : " + dbProduct.getId());

        return dbProduct;
    }

    public Page<Product> getProductsByCategory(GetProductsByCategoryInput getProductsByCategoryInput) {
        Category dbCategory = categoryRepository.findById(getProductsByCategoryInput.getCategoryId()).orElseThrow(CustomExceptions::categoryNotFoundException);
        int page = getProductsByCategoryInput.getPaginationInput().getPage();
        int size = getProductsByCategoryInput.getPaginationInput().getSize();
        String fieldName = getProductsByCategoryInput.getPaginationInput().getFieldName();
        SortBy sortBy = getProductsByCategoryInput.getPaginationInput().getSortBy();
        Pageable pageable = PageRequest.of(page
                , size
                , Sort.by(Sort.Direction.valueOf(sortBy.toString()), fieldName));


        return productRepository.findByCategoryId(dbCategory.getId(), pageable);
    }

    @Transactional
    public void updateProduct(UpdateProductInput updateProductInput, Product product) {

        if (updateProductInput.getName() != null) {
            Product productNameFromInput = productRepository.findByName(updateProductInput.getName().toLowerCase()).orElse(null);
            if (productNameFromInput == null) {
                product.setName(updateProductInput.getName().toLowerCase());
            } else {
                throw CustomExceptions.productNameIsAlreadyExistException();
            }

        }

        if (updateProductInput.getCategoryId() != 0) {
            Category category = categoryRepository.findById(updateProductInput.getCategoryId()).orElseThrow(CustomExceptions::categoryNotFoundException);
            product.setCategoryId(category.getId());
        }

        if (updateProductInput.getQuantity() != 0) {
            int newQuantity = product.getQuantity() + updateProductInput.getQuantity();
            product.setQuantity(newQuantity);
        }
        if (updateProductInput.getCriticalLevel() != 0) {
            product.setCriticalLevel(updateProductInput.getCriticalLevel());
        }
    }

    public List<StoreProductDTO> getProductByAttribute(List<Integer> storeIds, int productId) {

        List<StoreProduct> storeProductList = storeProductRepository.findAllByIdIn(storeIds, productId);
        List<Integer> storeIDsList = storeProductList.stream()
                .map(StoreProduct::getStore_id).collect(Collectors.toList());

        List<Store> storeList = storeRepository.findAllByIdIn(storeIDsList);

        return storeList.stream()
                .map(store -> {
                    int id = store.getId();
                    String name = store.getName();
                    Regions region = store.getRegion();
                    Cities city = store.getCity();
                    String address = store.getAddress();

                    StoreProduct storeProduct = storeProductList.stream()
                            .filter(sp -> sp.getStore_id() == id)
                            .findFirst()
                            .orElseThrow(CustomExceptions::storeNotFoundException);

                    StoreProductDTO dto = new StoreProductDTO();
                    dto.setId(id);
                    dto.setName(name);
                    dto.setRegion(region);
                    dto.setCity(city);
                    dto.setAddress(address);
                    dto.setProduct_id(storeProduct.getProduct_id());
                    dto.setQuantity(storeProduct.getQuantity());
                    return dto;
                }).collect(Collectors.toList());
    }
}
