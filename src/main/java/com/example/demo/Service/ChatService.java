package com.example.demo.Service;

import com.example.demo.entity.Message;
import com.example.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final
    MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getChatHistory(Integer senderId, Integer receiverId) {
        return messageRepository.findBySenderIdAndReceiverIdOrderByCreatedAtAsc(senderId, receiverId);
    }
}