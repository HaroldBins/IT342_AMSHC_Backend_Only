package com.example.appointmentsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean isRead = false;
}
