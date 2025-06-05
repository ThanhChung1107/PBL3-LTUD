package com.ProjectPBL3.MegarMart.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Setter
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RatingLevel rating;
    String comment;
    @ManyToOne
    @JoinColumn(name = "account_id",referencedColumnName = "id",nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id", nullable = false)
    private Product product;
    private String sellerReply;
    @OneToOne(mappedBy = "review")
    private OrderDetail orderDetail;
    @Column(nullable = false, updatable = false)
    LocalDate createdAt;

    @Column(nullable = false)
    LocalDate updatedAt;


    // Tự động set ngày tạo trước khi insert
    @PrePersist
    public void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }
    // Tự động cập nhật ngày sửa trước khi update
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }


}
