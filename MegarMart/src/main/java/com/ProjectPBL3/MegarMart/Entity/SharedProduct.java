package com.ProjectPBL3.MegarMart.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SharedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @OneToOne
    @JoinColumn(name = "message_id")
     Message message;

    Long productId; // Liên kết với ID sản phẩm trong hệ thống chính
    String productName;
    String productImage;
    Double productPrice;

    @ManyToOne
    @JoinColumn(name = "shared_by")
     Account sharedBy;
}
