package com.ProjectPBL3.MegarMart.Entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;
    String description;
    String imageurl;

    int price;

    int revenue;

    int stock;
    int status;
    int sold;


    @ManyToOne
    @JoinColumn(name = "shop_id",referencedColumnName = "id")
    Shop shop;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id")
    Category category;


    @Column(nullable = false, updatable = false)
    LocalDate createdAt;

    @Column(nullable = false)
    LocalDate updatedAt;


    // Tự động set ngày tạo trước khi insert
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    // Tự động cập nhật ngày sửa trước khi update
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}
