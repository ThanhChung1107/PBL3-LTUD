package com.ProjectPBL3.MegarMart.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int quantity;
    private int price;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;
    @OneToOne
    @JoinColumn(name = "review_id")
    private ReviewProduct review;
}
