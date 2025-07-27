package com.ProjectPBL3.MegarMart.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private ChatGroup group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account user;

    private LocalDateTime joinedAt;
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN,
        MEMBER
    }
}
