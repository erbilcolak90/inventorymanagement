# inventory management

The purpose of this project is to provide an inventory management system.

### Technologies and Tools Used

- Java 8
- Spring Boot 2.3.0
- Mysql
- Junit
- Mockito
- Swagger v2
- Lombok
- Slf4j

The project aims to facilitate inventory management by enabling the sending of products to warehouses,
recalling products from warehouses,
determining the number of warehouses in each city or region of the country,
and tracking the products in these warehouses.

Functions

Add Product: Allows the addition of new products to the system.
Update Product: Used to update the information of existing products.
Remove Product: Used to remove products from the system.
Warehouse Count: Provides the ability to view the number of warehouses nationwide and the products in each warehouse.
Product Quantity Alert: Issues a warning when the quantity of a product falls below a critical level.
Filter System: Use the filter system for searching products.

---

How to Run?

Download the project files to your computer.
Open a terminal or command prompt in the project root directory.
Start the project by using the command

>``` docker-compose up -d or docker-compose up```

inventorymanagement.sql file will be uploaded automatically and will be ready for use as a database.
After ensuring that the containers are up and running with Docker Compose,
Please check database proceed to http://localhost:8081/ to access phpMyAdmin. 

You can generate requests with dummy data.

Then visit http://localhost:8080/swagger-ui/index.html in your browser to explore the Swagger API documentation and use requests.

[Click Here For Postman Collection.zip](https://github.com/erbilcolak90/inventorymanagement/files/13511147/inventorymanagement-postman-collection.zip)

---

### Entities

- BaseEntity
````
createDate: Date
updateDate: Date
isDeleted: boolean
````

- Category

````
id: int
name: String
products: List
````

- Product
````
id: int
name: String
categoryId: int
quantity : int
criticalLevel : int
````

- Store
````
id : int
name : String
address : String
region : <Enum>
city : <Enum>
````

- StoreProduct
````
id: int
store_id: int
product_id: int
quantity: int
````


### Rest API

````
RestController("/product")
RestController("/store")
RestController("/category)

````

---

## RestController("/product")

### GetProductById

#### Request
````
method: POST
url: /product/getProductById
requestParams : -
requestBody: getProductByIdInput{

id: int

}
````

### Response

````
{
  "createDate": "2023-12-20T20:00:27.000+00:00",
  "updateDate": "2023-12-20T20:46:54.000+00:00",
  "id": 1,
  "name": "gömlek",
  "categoryId": 1,
  "quantity": 1000,
  "criticalLevel": 2000,
  "deleted": false
}
````

---

### GetProductByCity

#### Request
````
method: POST
url: /product/getProductByCity
requestParams : -
requestBody: getProductsByCityInput{

{
  "city": "ADANA",
  "productId": 0
}

}
````

### Response

````
[
  {
    "address": "string",
    "city": "ADANA",
    "id": 0,
    "name": "string",
    "product_id": 0,
    "quantity": 0,
    "region": "AKDENIZ"
  }
]
````

---

### GetProductByRegion

#### Request
````
method: POST
url: /product/getProductByRegion
requestParams : -
requestBody: getProductsByRegionInput{

{
  "productId": 0,
  "region": "AKDENIZ"
}
}
````

### Response

````
[
  {
    "address": "string",
    "city": "ADANA",
    "id": 0,
    "name": "string",
    "product_id": 0,
    "quantity": 0,
    "region": "AKDENIZ"
  }
]
````

---

### GetProductInStore

#### Request
````
method: POST
url: /product/getProductInStore
requestParams : -
requestBody: getProductInStoreInput{

{
  "productId": 0,
  "storeId": 0
}
}
````

### Response

````
{
  "id": 0,
  "product_id": 0,
  "quantity": 0,
  "store_id": 0
}
````

---

### GetProductsByCategory

#### Request
````
method: POST
url: /product/getProductsByCategory
requestParams : -
requestBody: getProductsByCategoryInput{

{
  "categoryId": 0,
  "paginationInput": {
    "fieldName": "string",
    "page": 0,
    "size": 0,
    "sortBy": "ASC"
  }
}

}
````

### Response

````
{
 {
  "content": [
    {
      "categoryId": 0,
      "createDate": "2023-11-30T08:31:50.041Z",
      "criticalLevel": 0,
      "deleted": true,
      "id": 0,
      "name": "string",
      "quantity": 0,
      "updateDate": "2023-11-30T08:31:50.041Z"
    }
  ],
  "empty": true,
  "first": true,
  "last": true,
  "number": 0,
  "numberOfElements": 0,
  "pageable": {
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 0,
    "paged": true,
    "sort": {
      "empty": true,
      "sorted": true,
      "unsorted": true
    },
    "unpaged": true
  },
  "size": 0,
  "sort": {
    "empty": true,
    "sorted": true,
    "unsorted": true
  },
  "totalElements": 0,
  "totalPages": 0
}
}
````

---

### CreateProduct

#### Request
````
method: POST
url: /product/createProduct
requestParams : -
requestBody: CreateProductInput{

name: String
categoryId: int
quantity: int
criticalLevel: int

}
````

### Response

````
{
  "createDate": "2024-01-07T19:12:14.090+00:00",
  "updateDate": "2024-01-07T19:12:14.090+00:00",
  "id": 8,
  "name": "telefon",
  "categoryId": 1,
  "quantity": 100,
  "criticalLevel": 10,
  "deleted": false
}
````

---

### DeleteProduct

#### Request
````
method: POST
url: /product/deleteProduct
requestParams : -
requestBody: deleteProductInput{

id: int

}
````

### Response

````

   {
  "deleted": true,
  "message": "string"
    }
````

---

### UpdateProduct

#### Request
````
method: PUT
url: /product/updateProduct
requestParams : -
requestBody: updateProductInput{
{
  "categoryId": 0,
  "criticalLevel": 0,
  "id": 0,
  "name": "string",
  "quantity": 0
}
}
````

### Response

````
{
 {
  "categoryId": 0,
  "createDate": "2023-11-30T08:33:43.090Z",
  "criticalLevel": 0,
  "deleted": true,
  "id": 0,
  "name": "string",
  "quantity": 0,
  "updateDate": "2023-11-30T08:33:43.090Z"
}
}
````

---

## RestController("/store")

### AddProductStore

#### Request
````
method: POST
url: /store/addProductStore
requestParams : -
requestBody: addProductStoreInput{
{
  "productId": 0,
  "quantity": 0,
  "storeId": 0
}
}
````

### Response

````
{
 {
  "id": 0,
  "product_id": 0,
  "quantity": 0,
  "store_id": 0
}
}
````

---

### CreateStore

#### Request
````
method: POST
url: /store/createStore
requestParams : -
requestBody: createStoreInput{
{
  "address": "string",
  "city": "ADANA",
  "name": "string",
  "region": "AKDENIZ"
}
}
````

### Response

````
{
 {
  "address": "string",
  "city": "ADANA",
  "createDate": "2024-01-08T13:07:11.937Z",
  "deleted": true,
  "id": 0,
  "name": "string",
  "region": "AKDENIZ",
  "updateDate": "2024-01-08T13:07:11.937Z"
}
}
````

---

### removeProductToStore

#### Request
````
method: POST
url: /store/createStore
requestParams : -
requestBody: createStoreInput{
{
  "productId": 0,
  "quantity": 0,
  "storeId": 0
}
}
````

### Response

````
{
{
  "id": 0,
  "product_id": 0,
  "quantity": 0,
  "store_id": 0
}
}
````

---

## RestController("/category)

### CreateCategory

#### Request
````
method: POST
url: /category/createCategory
requestParams : -
requestBody: createCategoryInput{
{
  "name": "string"
}
}
````

### Response

````
{
{
  "createDate": "2023-11-30T08:40:24.248Z",
  "deleted": true,
  "id": 0,
  "name": "string",
  "products": null,
  "updateDate": "2023-11-30T08:40:24.248Z"
}
}
````

---








