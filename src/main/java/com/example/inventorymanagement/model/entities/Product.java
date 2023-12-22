package com.example.inventorymanagement.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Product")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "storeId")
    private Store store;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "criticalLevel")
    private int criticalLevel;


}
