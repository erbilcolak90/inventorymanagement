package com.example.inventorymanagement.entities;

import com.example.inventorymanagement.enums.Cities;
import com.example.inventorymanagement.enums.Regions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Store")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @Column(name = "products")
    private List<Integer> products;
}
