package com.ProjectPBL3.MegarMart.Repository;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.ChatGroup;
import com.ProjectPBL3.MegarMart.Entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Integer> {
    boolean existsByGroupAndUser(ChatGroup group, Account user);

    List<GroupMember> findByGroup(ChatGroup group);

    Optional<GroupMember> findByGroupAndUser(ChatGroup group, Account user);

    // Kiểm tra user đã là member của group chưa
    @Query("SELECT COUNT(m) > 0 FROM GroupMember m WHERE m.user.id = :userId AND m.group.id = :groupId")
    boolean existsByUserIdAndGroupId(@Param("userId") Integer userId, @Param("groupId") Integer groupId);

    // Lấy tất cả members của một group
    @Query("SELECT m FROM GroupMember m WHERE m.group.id = :groupId")
    List<GroupMember> findByGroupId(@Param("groupId") Integer groupId);
}
