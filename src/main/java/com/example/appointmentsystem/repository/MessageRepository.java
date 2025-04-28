package com.example.appointmentsystem.repository;

import com.example.appointmentsystem.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
            "(m.senderId = :user1 AND m.receiverId = :user2) OR " +
            "(m.senderId = :user2 AND m.receiverId = :user1)")
    Page<Message> findConversation(Long user1, Long user2, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.receiverId = :receiverId AND m.senderId = :senderId AND m.isRead = false")
    List<Message> findUnreadByReceiverAndSender(Long receiverId, Long senderId);

    void deleteBySenderIdOrReceiverId(Long senderId, Long receiverId);

    @Query("SELECT DISTINCT CASE WHEN m.senderId = :userId THEN m.receiverId ELSE m.senderId END FROM Message m WHERE m.senderId = :userId OR m.receiverId = :userId")
    List<Long> findChatPartnerIds(Long userId);


    

}
