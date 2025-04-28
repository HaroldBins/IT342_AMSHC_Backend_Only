package com.example.appointmentsystem.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private String senderName;
    private String receiverName;
    private String senderRole;
    private String content;
    private LocalDateTime timestamp;
    private boolean read;
    private String senderAvatarUrl; // 🛠 Add this field
}
