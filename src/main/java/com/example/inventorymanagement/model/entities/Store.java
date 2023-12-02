package com.example.inventorymanagement.model.entities;

import com.example.inventorymanagement.model.enums.Cities;
import com.example.inventorymanagement.model.enums.Regions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "Store")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Store extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "region")
    @Enumerated(EnumType.STRING)
    private Regions region;
    @Column(name = "city")
    @Enumerated(EnumType.STRING)
    private Cities city;
    @ElementCollection
    @CollectionTable(name = "store_products",joinColumns = @JoinColumn(name = "storeId"))
    @MapKeyColumn(name = "productId")
    @Column(name = "quantity")
    private Map<Integer,Integer> products;
}
