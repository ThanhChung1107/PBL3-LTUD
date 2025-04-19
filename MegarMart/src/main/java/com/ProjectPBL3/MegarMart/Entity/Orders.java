package com.ProjectPBL3.MegarMart.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String phone;
    private String address;

    private int isPaid;
    private int totalprice;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    LocalDate createdAt;

    // Tự động set ngày tạo trước khi insert
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}
