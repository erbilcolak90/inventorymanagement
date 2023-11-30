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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "categoryId")
    private int categoryId;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "criticalLevel")
    private int criticalLevel;

}