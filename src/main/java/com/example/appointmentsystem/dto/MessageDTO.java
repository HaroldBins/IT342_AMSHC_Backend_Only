package com.example.appointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageDTO {
    private Long messageId;
    private String senderName;
    private String receiverName;
    private String senderRole;
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;
}
