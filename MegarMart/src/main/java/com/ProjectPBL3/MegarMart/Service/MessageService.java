package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.Message;
import com.ProjectPBL3.MegarMart.Entity.ChatGroup;
import com.ProjectPBL3.MegarMart.Entity.SharedProduct;
import com.ProjectPBL3.MegarMart.Repository.AccountRepository;
import com.ProjectPBL3.MegarMart.Repository.ChatGroupRepository;
import com.ProjectPBL3.MegarMart.Repository.MessageRepository;
import com.ProjectPBL3.MegarMart.Repository.SharedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MessageService {

//    @Autowired
private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;
    private final ChatGroupRepository groupRepository;
    private final SharedProductRepository sharedProductRepository;

    @Autowired
    public MessageService(
            MessageRepository messageRepository,
            AccountRepository accountRepository,
            ChatGroupRepository groupRepository,
            SharedProductRepository sharedProductRepository
    ) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
        this.groupRepository = groupRepository;
        this.sharedProductRepository = sharedProductRepository;
    }

    /**
     * Lưu tin nhắn mới
     */
    public Message save(Message message) {
        if (message.getCreatedAt() == null) {
            message.setCreatedAt(LocalDateTime.now());
        }
        return messageRepository.save(message);
    }

    /**
     * Tìm tin nhắn theo ID
     */
    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }

    /**
     * Lấy tất cả tin nhắn của một nhóm, sắp xếp theo thời gian tạo
     */
    public List<Message> getMessagesByGroup(Long groupId) {
        return messageRepository.findByGroupIdOrderByCreatedAtAsc(groupId);
    }
    @Transactional
    public Message shareProduct(Long groupId, Long senderId, SharedProduct productData) {
        ChatGroup group = groupRepository.findById(Math.toIntExact(groupId))
                .orElseThrow(() -> new RuntimeException("Group not found"));
        Account sender = accountRepository.findById(Math.toIntExact(senderId))
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Message message = new Message();
        message.setGroup(group);
        message.setSender(sender);
        message.setSenderName(sender.getUsername());
        message.setSenderAvatar(sender.getImageurl());
        message.setType(Message.MessageType.PRODUCT_SHARE);
        message.setCreatedAt(LocalDateTime.now());

        // Lưu trước message
        Message savedMessage = messageRepository.save(message);

        // Tạo SharedProduct gắn vào message
        SharedProduct sharedProduct = SharedProduct.builder()
                .message(savedMessage)
                .productId(productData.getProductId())
                .productName(productData.getProductName())
                .productImage(productData.getProductImage())
                .productPrice(productData.getProductPrice())
                .sharedBy(sender)
                .build();

        savedMessage.setSharedProduct(sharedProductRepository.save(sharedProduct));

        return savedMessage;
    }

    /**
     * Lấy tin nhắn của nhóm theo ID
     */
    public List<Message> getMessagesByGroupId(Long groupId) {
        return messageRepository.findByGroupIdOrderByCreatedAtAsc(groupId);
    }

    /**
     * Lấy tin nhắn với phân trang
     */
    public Page<Message> getMessagesByGroup(ChatGroup group, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        return messageRepository.findByGroup(group, pageable);
    }

    /**
     * Lấy tin nhắn gần đây nhất của nhóm
     */
    public List<Message> getRecentMessages(ChatGroup group, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        Page<Message> messages = messageRepository.findByGroup(group, pageable);
        return messages.getContent();
    }

    /**
     * Lấy tin nhắn gần đây nhất của nhóm theo ID
     */
    public List<Message> getRecentMessagesByGroupId(Long groupId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        return messageRepository.findByGroupIdOrderByCreatedAtDesc(groupId, pageable);
    }

    /**
     * Đếm số lượng tin nhắn trong nhóm
     */
    public long countMessagesByGroup(ChatGroup group) {
        return messageRepository.countByGroup(group);
    }

    /**
     * Xóa tin nhắn theo ID
     */
    public void deleteById(Long id) {
        messageRepository.deleteById(id);
    }

    /**
     * Xóa tất cả tin nhắn của một nhóm
     */
    public void deleteAllByGroup(ChatGroup group) {
        messageRepository.deleteAllByGroup(group);
    }

    /**
     * Tìm tin nhắn theo nội dung
     */
    public List<Message> searchMessages(String keyword, ChatGroup group) {
        return messageRepository.findByContentContainingIgnoreCaseAndGroupOrderByCreatedAtDesc(keyword, group);
    }

    /**
     * Lấy tin nhắn theo loại
     */
    public List<Message> getMessagesByType(ChatGroup group, Message.MessageType type) {
        return messageRepository.findByGroupAndTypeOrderByCreatedAtAsc(group, type);
    }

    /**
     * Lấy tin nhắn sau một thời điểm nhất định
     */
    public List<Message> getMessagesAfter(ChatGroup group, LocalDateTime after) {
        return messageRepository.findByGroupAndCreatedAtAfterOrderByCreatedAtAsc(group, after);
    }

    /**
     * Lấy tin nhắn trong khoảng thời gian
     */
    public List<Message> getMessagesBetween(ChatGroup group, LocalDateTime start, LocalDateTime end) {
        return messageRepository.findByGroupAndCreatedAtBetweenOrderByCreatedAtAsc(group, start, end);
    }

    /**
     * Kiểm tra xem tin nhắn có tồn tại không
     */
    public boolean existsById(Long id) {
        return messageRepository.existsById(id);
    }

    /**
     * Cập nhật nội dung tin nhắn
     */
    public Message updateContent(Long messageId, String newContent) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            message.setContent(newContent);
            return messageRepository.save(message);
        }
        return null;
    }

    /**
     * Lấy tin nhắn cuối cùng của nhóm
     */
    public Optional<Message> getLastMessage(ChatGroup group) {
        List<Message> messages = messageRepository.findTopByGroupOrderByCreatedAtDesc(group);
        return messages.isEmpty() ? Optional.empty() : Optional.of(messages.get(0));
    }

    /**
     * Lấy tin nhắn cuối cùng của nhóm theo ID
     */
    public Optional<Message> getLastMessageByGroupId(Long groupId) {
        List<Message> messages = messageRepository.findTopByGroupIdOrderByCreatedAtDesc(groupId);
        return messages.isEmpty() ? Optional.empty() : Optional.of(messages.get(0));
    }
}