package com.example.appointmentsystem.service;

import com.example.appointmentsystem.dto.MessageDTO;
import com.example.appointmentsystem.dto.MessageRequestDTO;
import com.example.appointmentsystem.model.AppUser;
import com.example.appointmentsystem.model.Doctor;
import com.example.appointmentsystem.model.Message;
import com.example.appointmentsystem.model.NotificationType;
import com.example.appointmentsystem.repository.DoctorRepository;
import com.example.appointmentsystem.repository.MessageRepository;
import com.example.appointmentsystem.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final DoctorRepository doctorRepository;
    private final AppUserRepository appUserRepository;
    private final NotificationService notificationService;

    public Message sendMessage(MessageRequestDTO dto) {
        // Fallback in case role is not provided
        String senderRole = dto.getSenderRole();
        if (senderRole == null || senderRole.isBlank()) {
            senderRole = appUserRepository.findById(dto.getSenderId())
                    .map(AppUser::getRole)
                    .orElse("UNKNOWN");
        }

        Message message = Message.builder()
                .senderId(dto.getSenderId())
                .receiverId(dto.getReceiverId())
                .senderRole(senderRole)
                .content(dto.getContent())
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        Message savedMessage = messageRepository.save(message);

        // 🔔 Auto-create notification for the receiver
        String content = "You have a new message from " + senderRole;
        notificationService.createNotification(
                dto.getReceiverId(),
                content,
                NotificationType.MESSAGE
        );

        return savedMessage;
    }

    public Page<MessageDTO> getConversation(Long user1, Long user2, Pageable pageable) {
        Page<Message> messages = messageRepository.findConversation(user1, user2, pageable);

        return messages.map(message -> {
            String senderName = getNameById(message.getSenderId(), message.getSenderRole());
            String receiverRole = "PATIENT";
            if ("PATIENT".equalsIgnoreCase(message.getSenderRole())) {
                receiverRole = "DOCTOR";
            }

            String receiverName = getNameById(message.getReceiverId(), receiverRole);

            return new MessageDTO(
                    message.getId(),
                    senderName,
                    receiverName,
                    message.getSenderRole(),
                    message.getContent(),
                    message.getTimestamp(),
                    message.isRead()
            );
        });
    }

    public void markMessagesAsRead(Long receiverId, Long senderId) {
        List<Message> messages = messageRepository.findUnreadByReceiverAndSender(receiverId, senderId);
        for (Message message : messages) {
            message.setRead(true);
        }
        messageRepository.saveAll(messages);
    }

    private String getNameById(Long id, String role) {
        if (role == null) {
            return "Unknown User";
        }

        if (role.equalsIgnoreCase("DOCTOR")) {
            return doctorRepository.findById(id)
                    .map(Doctor::getName)
                    .map(name -> "Dr. " + name)
                    .orElse("Unknown Doctor");
        } else if (role.equalsIgnoreCase("PATIENT")) {
            return appUserRepository.findById(id)
                    .map(AppUser::getFullName)
                    .orElse("Unknown Patient");
        }

        return "Unknown Role";
    }

    public void deleteMessage(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // Allow only the sender or receiver to delete the message
        if (!message.getSenderId().equals(userId) && !message.getReceiverId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this message.");
        }

        messageRepository.deleteById(messageId);
    }
}
