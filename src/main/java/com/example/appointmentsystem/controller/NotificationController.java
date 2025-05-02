package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.model.AppUser;
import com.example.appointmentsystem.model.Notification;
import com.example.appointmentsystem.model.NotificationType;
import com.example.appointmentsystem.security.CustomUserDetails;
import com.example.appointmentsystem.service.NotificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/create")
    public ResponseEntity<Notification> createNotification(
            @RequestParam Long userId,
            @RequestParam String content,
            @RequestParam NotificationType type
    ) {
        return ResponseEntity.ok(notificationService.createNotification(userId, content, type));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsForUser(userId));
    }

    @PutMapping("/read/{notificationId}")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read.");
    }

    @PutMapping("/read-all/{userId}")
    public ResponseEntity<String> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok("All notifications marked as read.");
    }

    @DeleteMapping("/{notificationId}")
public ResponseEntity<String> deleteNotification(
        @PathVariable Long notificationId,
        @AuthenticationPrincipal CustomUserDetails userDetails
) {
    Long userId = userDetails.getUserId();

    // ✅ This line helps you confirm if the controller is actually being reached
    System.out.println("✅ Deleting notification for userId: " + userId);
    System.out.println("🔐 DELETE endpoint reached");
System.out.println("User ID: " + userDetails.getUserId());
System.out.println("User role: " + userDetails.getRole());


    notificationService.deleteNotification(notificationId, userId);
    return ResponseEntity.ok("Notification deleted successfully.");
}


@GetMapping("/test")
public ResponseEntity<String> testAccess(@AuthenticationPrincipal CustomUserDetails userDetails) {
    if (userDetails == null) {
        return ResponseEntity.status(401).body("❌ Not authenticated");
    }
    return ResponseEntity.ok("✅ Authenticated as userId: " + userDetails.getUserId() + ", role: " + userDetails.getRole());
}


}
