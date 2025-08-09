package com.ProjectPBL3.MegarMart.Repository;

import com.ProjectPBL3.MegarMart.Entity.ChatGroup;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatGroupRepository extends JpaRepository<ChatGroup,Integer> {
    @Query("SELECT DISTINCT g FROM ChatGroup g JOIN g.members m WHERE m.user.id = :userId")
    List<ChatGroup> findByMembers_User_Id(@Param("userId") Integer userId);

    @Query("SELECT g FROM ChatGroup g " +
            "WHERE g.id IN (SELECT m.group.id FROM GroupMember m WHERE m.user.id = :userId)")
    List<ChatGroup> findGroupsByUserId(@Param("userId") Integer userId);

    @Query("SELECT g FROM ChatGroup g WHERE g.groupcode = :groupcode")
    Optional<ChatGroup> findByInviteCode(@Param("groupcode") String groupcode);

    // Kiểm tra invite code có tồn tại không
    @Query("SELECT COUNT(g) > 0 FROM ChatGroup g WHERE g.groupcode = :groupcode")
    boolean existsByGroupcode(@Param("groupcode") String inviteCode);

    @Query("SELECT COUNT(gm) FROM GroupMember gm WHERE gm.group.id = :groupId")
    int MemberCount(@Param("groupId") Long groupId);

    ChatGroup findGroupById(Integer id);
}
