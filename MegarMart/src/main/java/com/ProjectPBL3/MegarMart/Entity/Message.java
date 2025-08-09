package com.ProjectPBL3.MegarMart.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    public enum MessageType {
        TEXT,          // Tin nhắn văn bản thông thường
        PRODUCT_SHARE,
        JOIN,
        LEAVE// Tin nhắn chia sẻ sản phẩm
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    ChatGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    Account sender;
    @Transient
    private String senderName; // Tên người gửi để hiển thị

    @Transient
    private String senderAvatar; // Avatar người gửi
    @Column(columnDefinition = "TEXT")
    String content;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MessageType type;

    @OneToOne(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    SharedProduct sharedProduct;

    // Helper methods
    public static Message createTextMessage(ChatGroup group, Account sender, String content) {
        return Message.builder()
                .group(group)
                .sender(sender)
                .content(content)
                .type(MessageType.TEXT)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Message createProductShareMessage(ChatGroup group, Account sender, SharedProduct sharedProduct) {
        Message message = Message.builder()
                .group(group)
                .sender(sender)
                .type(MessageType.PRODUCT_SHARE)
                .createdAt(LocalDateTime.now())
                .sharedProduct(sharedProduct)
                .build();
        sharedProduct.setMessage(message);
        return message;
    }
}