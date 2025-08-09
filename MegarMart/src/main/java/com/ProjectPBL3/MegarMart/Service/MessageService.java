package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.Message;
import com.ProjectPBL3.MegarMart.Entity.ChatGroup;
import com.ProjectPBL3.MegarMart.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

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
    public List<Message> getMessagesByGroup(ChatGroup group) {
        return messageRepository.findByGroupOrderByCreatedAtAsc(group);
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