package com.example.appointmentsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;

    private Long receiverId;

    private String senderRole; // "DOCTOR" or "PATIENT"

    private String content;

    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean isRead = false;

}
