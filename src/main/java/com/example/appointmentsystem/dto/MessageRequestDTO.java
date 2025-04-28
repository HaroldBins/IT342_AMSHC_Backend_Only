package com.example.appointmentsystem.dto;

import lombok.Data;

@Data
public class MessageRequestDTO {
    private Long senderId;
    private Long receiverId;
    private String senderRole;
    private String content;
}
