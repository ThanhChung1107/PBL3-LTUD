package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.Message;
import com.ProjectPBL3.MegarMart.Entity.Message.MessageType;
import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.ChatGroup;
import com.ProjectPBL3.MegarMart.Entity.SharedProduct;
import com.ProjectPBL3.MegarMart.Service.MessageService;
import com.ProjectPBL3.MegarMart.Service.AccountService;
import com.ProjectPBL3.MegarMart.Service.ChatGroupService;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ChatGroupService chatGroupService;

    @MessageMapping("/chat/{groupId}/sendMessage")
    @SendTo("/topic/group/{groupId}")
    public Message sendMessage(@DestinationVariable String groupId, @Payload Map<String, Object> payload) {
        try {
            String content = (String) payload.get("content");
            String senderName = (String) payload.get("senderName");
            String senderAvatar = (String) payload.get("senderAvatar");

            // Tạo message object để gửi qua WebSocket
            Message message = new Message();
            message.setContent(content);
            message.setSenderName(senderName);
            message.setType(MessageType.TEXT);
            message.setCreatedAt(LocalDateTime.now());
            message.setSenderAvatar(senderAvatar);

            // Lưu vào database (optional - có thể bỏ comment nếu muốn lưu)
            try {
                Account sender = accountService.findByUsername(senderName);
                ChatGroup group = chatGroupService.findById2(Integer.parseInt(groupId));

                if (sender != null && group != null) {
                    Message dbMessage = Message.createTextMessage(group, sender, content);
                    messageService.save(dbMessage);
                }
            } catch (Exception e) {
                System.err.println("Error saving message to database: " + e.getMessage());
            }

            return message;

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            return null;
        }
    }

    @MessageMapping("/chat/{groupId}/join")
    @SendTo("/topic/group/{groupId}")
    public Message join(@DestinationVariable String groupId, @Payload Map<String, Object> payload) {
        try {
            String senderName = (String) payload.get("senderName");
            String joinTimeStr = (String) payload.get("joinTime");
            if (StringUtils.isEmpty(senderName) || StringUtils.isEmpty(joinTimeStr)) {
                throw new IllegalArgumentException("Invalid join payload");
            }

            LocalDateTime joinTime = LocalDateTime.parse(joinTimeStr);
            if (Duration.between(joinTime, LocalDateTime.now()).toSeconds() > 5) {
                return null; // Ignore if join time is too old
            }

            Message message = new Message();
            message.setType(MessageType.JOIN);
            message.setContent(senderName + " đã tham gia nhóm");
            message.setSenderName(senderName);
            message.setCreatedAt(LocalDateTime.now());

            return message;

        } catch (Exception e) {
            System.err.println("Error processing join message: " + e.getMessage());
            return null;
        }
    }

    @MessageMapping("/chat/{groupId}/leave")
    @SendTo("/topic/group/{groupId}")
    public Message leave(@DestinationVariable String groupId, @Payload Map<String, Object> payload) {
        try {
            String senderName = (String) payload.get("senderName");

            Message message = new Message();
            message.setSenderName(senderName);
            message.setType(MessageType.LEAVE);
            message.setContent(senderName + " đã rời nhóm");
            message.setCreatedAt(LocalDateTime.now());

            return message;

        } catch (Exception e) {
            System.err.println("Error processing leave message: " + e.getMessage());
            return null;
        }
    }

    @MessageMapping("/chat/{groupId}/shareProduct")
    @SendTo("/topic/group/{groupId}")
    public Message shareProduct(
            @DestinationVariable Long groupId,
            @Payload Map<String, Object> payload) {

        try {
            Long senderId = Long.parseLong(payload.get("senderId").toString());

            Map<String, Object> productMap = (Map<String, Object>) payload.get("product");

            SharedProduct product = SharedProduct.builder()
                    .productId(Long.parseLong(productMap.get("id").toString()))
                    .productName((String) productMap.get("name"))
                    .productImage((String) productMap.get("imageUrl"))
                    .productPrice(Double.parseDouble(productMap.get("price").toString()))
                    .build();

            return MessageService.shareProduct(groupId, senderId, product);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}