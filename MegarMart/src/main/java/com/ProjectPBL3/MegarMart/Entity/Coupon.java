package com.ProjectPBL3.MegarMart.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String code;
    int count;
    int discount;
    int usagelimit;
    int status;

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
