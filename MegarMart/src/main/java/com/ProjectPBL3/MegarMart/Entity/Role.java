package com.ProjectPBL3.MegarMart.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    @GeneratedValue
    @Column(name = "id")
    Integer Id;

    @Column(nullable = false, unique = true)
    String roleName;
}
