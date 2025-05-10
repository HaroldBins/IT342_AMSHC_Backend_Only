package com.example.appointmentsystem.service;

import com.example.appointmentsystem.model.AppUser;
import com.example.appointmentsystem.model.Notification;
import com.example.appointmentsystem.model.NotificationType;
import com.example.appointmentsystem.repository.AppUserRepository;
import com.example.appointmentsystem.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AppUserRepository userRepository;

    public Notification createNotification(Long userId, String content, NotificationType type) {
        Notification notification = Notification.builder()
                .userId(userId)
                .content(content)
                .type(type)
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    public List<Notification> getUnreadNotificationsForUser(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findByUserIdAndIsReadFalse(userId);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));

        if (!notification.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete notification");
        }

        notificationRepository.deleteById(notificationId);
    }

    public AppUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
