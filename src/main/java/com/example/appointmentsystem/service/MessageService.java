package com.example.appointmentsystem.service;

import com.example.appointmentsystem.dto.MessageDTO;
import com.example.appointmentsystem.dto.MessageRequestDTO;
import com.example.appointmentsystem.dto.UserSimpleDTO;
import com.example.appointmentsystem.model.AppUser;
import com.example.appointmentsystem.model.Doctor;
import com.example.appointmentsystem.model.Message;
import com.example.appointmentsystem.model.NotificationType;
import com.example.appointmentsystem.repository.AppUserRepository;
import com.example.appointmentsystem.repository.DoctorRepository;
import com.example.appointmentsystem.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final DoctorRepository doctorRepository;
    private final AppUserRepository appUserRepository;
    private final NotificationService notificationService;

    public Message sendMessage(MessageRequestDTO dto) {
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

        if ("PATIENT".equalsIgnoreCase(senderRole)) {
            doctorRepository.findById(dto.getReceiverId()).ifPresent(doctor -> {
                Long appUserId = doctor.getUser().getId();
                message.setReceiverId(appUserId);
            });
        } else if ("DOCTOR".equalsIgnoreCase(senderRole)) {
            appUserRepository.findById(dto.getReceiverId()).ifPresent(patient -> {
                message.setReceiverId(patient.getId());
            });
        }

        Message savedMessage = messageRepository.save(message);

        notificationService.createNotification(
                message.getReceiverId(),
                "You received a new message.",
                NotificationType.MESSAGE
        );

        return savedMessage;
    }

    public Page<MessageDTO> getConversation(Long user1, Long user2, Pageable pageable) {
        Page<Message> messages = messageRepository.findConversation(user1, user2, pageable);
    
        return messages.map(message -> {
            MessageDTO dto = new MessageDTO();
            dto.setMessageId(message.getId());
            dto.setSenderId(message.getSenderId());
            dto.setReceiverId(message.getReceiverId());
            dto.setSenderName(getNameById(message.getSenderId(), message.getSenderRole())); // 🛠 Use getNameById
            dto.setReceiverName(getNameById(message.getReceiverId(), getOppositeRole(message.getSenderRole()))); // 🛠 Fix receiver
            dto.setSenderRole(message.getSenderRole());
            dto.setContent(message.getContent());
            dto.setTimestamp(message.getTimestamp());
            dto.setRead(message.isRead());
    
            // 🛠 Use your existing getAvatarUrl (role-based)
            String senderAvatar = getAvatarUrl(message.getSenderId(), message.getSenderRole());
            dto.setSenderAvatarUrl(getAvatarUrl(message.getSenderId(), message.getSenderRole())); // ✅ ADD this if missing

    
            return dto;
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

    private String getAvatarUrl(Long id, String role) {
        if (role == null) {
            return null;
        }
        if (role.equalsIgnoreCase("DOCTOR")) {
            return doctorRepository.findById(id)
                    .map(Doctor::getUser)
                    .map(AppUser::getAvatarUrl)
                    .orElse(null);
        } else if (role.equalsIgnoreCase("PATIENT")) {
            return appUserRepository.findById(id)
                    .map(AppUser::getAvatarUrl)
                    .orElse(null);
        }
        return null;
    }

    public List<UserSimpleDTO> getChatPartners(Long userId) {
        List<Long> partnerIds = messageRepository.findChatPartnerIds(userId);
        List<AppUser> partners = appUserRepository.findAllById(partnerIds);

        return partners.stream()
                .map(user -> new UserSimpleDTO(
                        user.getId(),
                        user.getFullName(),
                        user.getAvatarUrl()
                ))
                .collect(Collectors.toList());
    }

    private String getUserAvatarById(Long userId) {
        return appUserRepository.findById(userId)
                .map(AppUser::getAvatarUrl)
                .orElse(null);
    }

    private String getOppositeRole(String senderRole) {
        if ("DOCTOR".equalsIgnoreCase(senderRole)) {
            return "PATIENT";
        } else if ("PATIENT".equalsIgnoreCase(senderRole)) {
            return "DOCTOR";
        }
        return null;
    }
    
    public void deleteMessage(Long messageId, Long userId) {
        // Dummy implementation for now
        // (you can implement real logic later)
        // Example: messageRepository.deleteById(messageId);
    }
    
}