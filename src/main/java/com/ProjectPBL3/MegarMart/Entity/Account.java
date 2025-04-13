package com.ProjectPBL3.MegarMart.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true)
    String username;
    String password;
    String name;
    @Column(nullable = false,unique = true)
    String email;
    @Column
    String phone;
    @Column
    String address;

    String imageurl;

    @ManyToOne(fetch = FetchType.LAZY) // Giảm truy vấn dư thừa
    @JoinColumn(name = "roleId",referencedColumnName = "id")
    Role role;

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
