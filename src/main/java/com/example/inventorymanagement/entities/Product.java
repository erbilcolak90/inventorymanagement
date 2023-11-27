package com.example.inventorymanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Product")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

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
    @Column(name = "createDate")
    private Date createDate = new Date();
    @Column(name = "updateDate")
    private Date updateDate = new Date();
    @Column(name = "isDeleted")
    private boolean isDeleted=false;
}
