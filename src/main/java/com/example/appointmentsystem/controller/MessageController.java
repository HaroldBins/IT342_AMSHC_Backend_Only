package com.example.appointmentsystem.controller;

import com.example.appointmentsystem.dto.MessageDTO;
import com.example.appointmentsystem.dto.MessageRequestDTO;
import com.example.appointmentsystem.model.Message;
import com.example.appointmentsystem.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequestDTO dto) {
        return ResponseEntity.ok(messageService.sendMessage(dto));
    }

    @GetMapping("/conversation")
    public ResponseEntity<Page<MessageDTO>> getConversation(
            @RequestParam Long user1,
            @RequestParam Long user2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                "desc".equalsIgnoreCase(sortDir)
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending()
        );

        return ResponseEntity.ok(messageService.getConversation(user1, user2, pageable));
    }

    @PutMapping("/mark-read")
    public ResponseEntity<String> markMessagesAsRead(
            @RequestParam Long receiverId,
            @RequestParam Long senderId
    ) {
        messageService.markMessagesAsRead(receiverId, senderId);
        return ResponseEntity.ok("Messages marked as read.");
    }

    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<String> deleteMessage(
            @PathVariable Long messageId,
            @RequestParam Long userId
    ) {
        messageService.deleteMessage(messageId, userId);
        return ResponseEntity.ok("Message deleted successfully.");
    }


}
