package com.example.demo.controller;

import com.example.demo.Service.ChatService;
import com.example.demo.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    public void sendMessage(@Payload Message message) {
        chatService.saveMessage(message);
        String destination = "/topic/messages/" + message.getReceiverId();
        messagingTemplate.convertAndSend(destination, message);
    }

    @GetMapping("/history/{senderId}/{receiverId}")
    public List<Message> getChatHistory(@PathVariable Integer senderId, @PathVariable Integer receiverId) {
        return chatService.getChatHistory(senderId, receiverId);
    }
}
