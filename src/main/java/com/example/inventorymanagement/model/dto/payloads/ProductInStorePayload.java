package com.example.inventorymanagement.model.dto.payloads;

import com.example.inventorymanagement.model.entities.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductInStorePayload {

    private int productId;
    private int quantity;
    private List<Store> storeList;

    public static ProductInStorePayload from(int productId, int quantity, List<Store> storeList){
        ProductInStorePayload productInStorePayload = new ProductInStorePayload();
        productInStorePayload.setProductId(productId);
        productInStorePayload.setQuantity(quantity);
        productInStorePayload.setStoreList(storeList);

        return productInStorePayload;
    }
}
