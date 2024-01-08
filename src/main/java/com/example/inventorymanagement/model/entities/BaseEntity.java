package com.example.inventorymanagement.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@MappedSuperclass
public class BaseEntity {

    @Column(name = "createDate")
    private Date createDate = new Date();
    @Column(name = "updateDate")
    private Date updateDate = new Date();
    @Column(name = "isDeleted")
    private boolean isDeleted = false;
}
