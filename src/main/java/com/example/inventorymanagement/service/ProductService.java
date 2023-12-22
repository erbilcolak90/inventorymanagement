package com.example.inventorymanagement.service;

import com.example.inventorymanagement.model.dto.inputs.*;
import com.example.inventorymanagement.model.entities.Category;
import com.example.inventorymanagement.model.entities.Product;
import com.example.inventorymanagement.model.entities.Store;
import com.example.inventorymanagement.model.enums.Actions;
import com.example.inventorymanagement.model.enums.SortBy;
import com.example.inventorymanagement.exception.CustomExceptions;
import com.example.inventorymanagement.model.dto.payloads.DeleteProductPayload;
import com.example.inventorymanagement.repository.CategoryRepository;
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final LogService logService;

    public Product getProductById(GetProductByIdInput getProductByIdInput) {

        Product dbProduct = productRepository.findByIdAndIsDeletedFalse(getProductByIdInput.getId()).orElseThrow(CustomExceptions::productNotFoundException);

        return dbProduct;
    }

   /* public List<Store> getProductsByStoreAttribute(GetProductByStoreAttributeInput getProductByStoreAttributeInput) {
        Product dbProduct = productRepository.findByIdAndIsDeletedFalse(getProductByStoreAttributeInput.getProductId()).orElseThrow(CustomExceptions::productNotFoundException);
        List<Integer> dbStoreIds = storeRepository.findStoreIdsByProductId(getProductByStoreAttributeInput.getProductId());
        List<Store> dbStoreList = storeRepository.findStoresByStoreIdsAndAttribute(dbStoreIds, getProductByStoreAttributeInput.getFilterField());

        //filteredStores is filtering store using productId then using peek for filtering to products in every store by input productIds.
        List<Store> filteredStores = dbStoreList.stream()
                .filter(store -> store.getProducts().containsKey(getProductByStoreAttributeInput.getProductId()))
                .peek(store -> {
                    Map<Integer, Integer> filteredProducts = new HashMap<>();
                    filteredProducts.put(getProductByStoreAttributeInput.getProductId(), store.getProducts().get(getProductByStoreAttributeInput.getProductId()));
                    store.setProducts(filteredProducts);
                })
                .collect(Collectors.toList());

        return filteredStores;
    }
*/
    @Transactional
    public Product createProduct(CreateProductInput createProductInput) throws CustomExceptions {
        Product dbProduct = productRepository.findByName(createProductInput.getName().toLowerCase()).orElse(null);
        Category dbCategory= categoryRepository.findById(createProductInput.getCategoryId()).orElseThrow(CustomExceptions::categoryNotFoundException);
        if (dbProduct == null) {
            Product newProduct = new Product();
            newProduct.setName(createProductInput.getName().toLowerCase());
            newProduct.setCategory(dbCategory);
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

        Page<Product> productsPage = productRepository.findByCategoryId(dbCategory.getId(),pageable);

        return productsPage;
    }

    @Transactional
    public void updateProduct(UpdateProductInput updateProductInput, Product product) {

        if (updateProductInput.getName() != null) {
            Product productNameFromInput = productRepository.findByName(updateProductInput.getName().toLowerCase()).orElse(null);
            if(productNameFromInput == null){
                product.setName(updateProductInput.getName().toLowerCase());
            }else{
                throw CustomExceptions.productNameIsAlreadyExistException();
            }

        }

        if (updateProductInput.getCategoryId() != 0) {
            Category dbCategory = categoryRepository.findById(updateProductInput.getCategoryId()).orElseThrow(CustomExceptions::categoryNotFoundException);
        }

        if (updateProductInput.getQuantity() != 0) {
            int newQuantity = product.getQuantity() + updateProductInput.getQuantity();
            product.setQuantity(newQuantity);
        }
        if (updateProductInput.getCriticalLevel() != 0) {
            product.setCriticalLevel(updateProductInput.getCriticalLevel());
        }
    }
}
