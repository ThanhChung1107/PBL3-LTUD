package com.ProjectPBL3.MegarMart.Repository;

import com.ProjectPBL3.MegarMart.Entity.Message;
import com.ProjectPBL3.MegarMart.Entity.ChatGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Tìm tất cả tin nhắn của nhóm, sắp xếp theo thời gian tạo tăng dần
     */
    List<Message> findByGroupOrderByCreatedAtAsc(ChatGroup group);

    /**
     * Tìm tất cả tin nhắn của nhóm theo ID, sắp xếp theo thời gian tạo tăng dần
     */
    @Query("SELECT m FROM Message m WHERE m.group.id = :groupId ORDER BY m.createdAt ASC")
    List<Message> findByGroupIdOrderByCreatedAtAsc(@Param("groupId") Long groupId);

    /**
     * Tìm tin nhắn của nhóm với phân trang
     */
    Page<Message> findByGroup(ChatGroup group, Pageable pageable);

    /**
     * Tìm tin nhắn gần đây của nhóm theo ID với phân trang
     */
    @Query("SELECT m FROM Message m WHERE m.group.id = :groupId ORDER BY m.createdAt DESC")
    List<Message> findByGroupIdOrderByCreatedAtDesc(@Param("groupId") Long groupId, Pageable pageable);

    /**
     * Đếm số lượng tin nhắn trong nhóm
     */
    long countByGroup(ChatGroup group);

    /**
     * Xóa tất cả tin nhắn của nhóm
     */
    void deleteAllByGroup(ChatGroup group);

    /**
     * Tìm kiếm tin nhắn theo nội dung
     */
    List<Message> findByContentContainingIgnoreCaseAndGroupOrderByCreatedAtDesc(String keyword, ChatGroup group);

    /**
     * Tìm tin nhắn theo loại và nhóm
     */
    List<Message> findByGroupAndTypeOrderByCreatedAtAsc(ChatGroup group, Message.MessageType type);

    /**
     * Tìm tin nhắn sau một thời điểm
     */
    List<Message> findByGroupAndCreatedAtAfterOrderByCreatedAtAsc(ChatGroup group, LocalDateTime after);

    /**
     * Tìm tin nhắn trong khoảng thời gian
     */
    List<Message> findByGroupAndCreatedAtBetweenOrderByCreatedAtAsc(ChatGroup group, LocalDateTime start, LocalDateTime end);

    /**
     * Lấy tin nhắn cuối cùng của nhóm
     */
    @Query("SELECT m FROM Message m WHERE m.group = :group ORDER BY m.createdAt DESC")
    List<Message> findTopByGroupOrderByCreatedAtDesc(@Param("group") ChatGroup group);

    /**
     * Lấy tin nhắn cuối cùng của nhóm theo ID
     */
    @Query("SELECT m FROM Message m WHERE m.group.id = :groupId ORDER BY m.createdAt DESC")
    List<Message> findTopByGroupIdOrderByCreatedAtDesc(@Param("groupId") Long groupId);

    Message findTop1ByGroupIdOrderByCreatedAtDesc(Long groupId);
    /**
     * Tìm tin nhắn theo người gửi và nhóm
     */
    @Query("SELECT m FROM Message m WHERE m.group = :group AND m.sender.id = :senderId ORDER BY m.createdAt ASC")
    List<Message> findByGroupAndSenderId(@Param("group") ChatGroup group, @Param("senderId") Long senderId);

    /**
     * Tìm tin nhắn chưa đọc sau thời điểm nhất định
     */
    @Query("SELECT m FROM Message m WHERE m.group = :group AND m.createdAt > :lastSeen ORDER BY m.createdAt ASC")
    List<Message> findUnreadMessages(@Param("group") ChatGroup group, @Param("lastSeen") LocalDateTime lastSeen);

    /**
     * Đếm tin nhắn chưa đọc
     */
    @Query("SELECT COUNT(m) FROM Message m WHERE m.group = :group AND m.createdAt > :lastSeen")
    long countUnreadMessages(@Param("group") ChatGroup group, @Param("lastSeen") LocalDateTime lastSeen);

    /**
     * Tìm tin nhắn chia sẻ sản phẩm
     */
    @Query("SELECT m FROM Message m WHERE m.group = :group AND m.type = 'PRODUCT_SHARE' ORDER BY m.createdAt DESC")
    List<Message> findProductShareMessages(@Param("group") ChatGroup group);

    /**
     * Lấy thống kê số lượng tin nhắn theo loại
     */
    @Query("SELECT m.type, COUNT(m) FROM Message m WHERE m.group = :group GROUP BY m.type")
    List<Object[]> getMessageCountByType(@Param("group") ChatGroup group);
}