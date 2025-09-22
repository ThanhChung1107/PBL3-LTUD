package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.ChatGroup;
import com.ProjectPBL3.MegarMart.Entity.GroupMember;
import com.ProjectPBL3.MegarMart.Entity.Message;
import com.ProjectPBL3.MegarMart.Repository.AccountRepository;
import com.ProjectPBL3.MegarMart.Repository.ChatGroupRepository;
import com.ProjectPBL3.MegarMart.Repository.GroupMemberRepository;
import com.ProjectPBL3.MegarMart.Repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatGroupService {
    private final ChatGroupRepository groupRepository;
    private final AccountRepository accountRepository;
    private final GroupMemberRepository memberRepository;
    private final FileSystemStorageService storageService;
    private final MessageRepository messageRepository;

    private String generateGroupCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }

    @Transactional
    public ChatGroup createGroup(String name, Account creator,  MultipartFile file){
        String groupCode = generateGroupCode();
        ChatGroup group = new ChatGroup();
        group.setGroupname(name);
        group.setCreator(creator);

        //xử lý hình ảnh
        if (file != null && !file.isEmpty()) {
            storageService.store(file);
            String filename = file.getOriginalFilename();
            group.setGroupavt(filename);
        } else {
            // Gán ảnh mặc định nếu không có ảnh upload
            group.setGroupavt("anhnhommacdinh.jpg");  // ảnh này phải có sẵn trong thư mục lưu trữ
        }


        group.setGroupcode(groupCode);
        group.setCreatedAt(LocalDateTime.now());

        ChatGroup saveGroup = groupRepository.save(group);

        addMember(saveGroup,creator,"ADMIN");

        return saveGroup;
    }
    @Transactional
    public GroupMember addMember(ChatGroup group, Account user, String role) {
        // Kiểm tra user đã ở trong nhóm chưa
        if (memberRepository.existsByUserIdAndGroupId((int) group.getId(), user.getId())) {
            return null;
        }

        GroupMember member = new GroupMember();
        member.setGroup(group);
        member.setUser(user);
        member.setRole(GroupMember.Role.valueOf(role));
        member.setJoinedAt(LocalDateTime.now());

        return memberRepository.save(member);
    }
//tìm kiếm các group của 1 user
    public List<ChatGroup> getGroupsByUser(Integer userId) {
        return groupRepository.findGroupsByUserId(userId);
    }

//join grouop by code
    public Optional<ChatGroup> findByCode(String code) {
        return groupRepository.findByInviteCode(code);
    }

    public boolean isUserInGroup(long groupId, Integer userId) {
        return memberRepository.existsByUserIdAndGroupId((int) groupId, userId);
    }

    public Optional<ChatGroup> findById(Integer id) {
        return groupRepository.findById(id);
    }
    public ChatGroup findById2(Integer id){
        return groupRepository.findGroupById(id);
    }
    public int getMemberCount(Long id){
        return groupRepository.MemberCount(id);
    }
//    public Map<Integer, String> getLastMessagesForGroups(List<ChatGroup> groups) {
//        return groups.stream()
//                .collect(Collectors.toMap(
//                        ChatGroup::getId,
//                        group -> messageRepo.findTopByGroupOrderByCreatedAtDesc(group)
//                                .map(Message::getContent)
//                                .orElse("Chưa có tin nhắn")
//                ));
//    }
public Map<Long, Message> getLastMessagesForGroups(List<ChatGroup> groups) {
    Map<Long, Message> lastMessages = new HashMap<>();

    for (ChatGroup group : groups) {
        List<Message> messages = messageRepository.findTopByGroupOrderByCreatedAtDesc(group);

        if (messages != null && !messages.isEmpty()) {
            lastMessages.put(group.getId(), messages.get(0)); // lấy tin nhắn mới nhất
        } else {
            lastMessages.put(group.getId(), null); // chưa có tin nhắn
        }
    }

    return lastMessages;
}
}
