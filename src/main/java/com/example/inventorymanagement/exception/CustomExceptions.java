package com.example.inventorymanagement.exception;

public class CustomExceptions extends RuntimeException{

    public CustomExceptions(String message) {
        super(message);
    }

    public static CustomExceptions productNotFoundException(){
        return new CustomExceptions("Product is not found");
    }

    public static CustomExceptions productNameIsAlreadyExistException() {
        return new CustomExceptions("Product name is already exist");
    }

    public static CustomExceptions categoryNameIsAlreadyExistException() {
        return new CustomExceptions("Category name is already exist");
    }

    public static CustomExceptions storeNameIsAlreadyExistException() {
        return new CustomExceptions("Store name is already exist");
    }

    public static CustomExceptions storeNotFoundException(){
        return new CustomExceptions("Store not found");
    }

    public static CustomExceptions categoryNotFoundException(){
        return new CustomExceptions("Category not found");
    }

    public static CustomExceptions productQuantityAtStoreLessThanInputQuantityException() {
        return new CustomExceptions("Quantity from Input may less or equal to product quantity at store");
    }

    public static CustomExceptions productNotInTheStoreException() {
        return new CustomExceptions("The product is not in the store");
    }

    public static CustomExceptions productQuantityLessThanInputQuantityException() {
        return new CustomExceptions("Product quantity may greater than input quantity");
    }
}
