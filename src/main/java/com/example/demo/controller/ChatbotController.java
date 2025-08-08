package com.example.demo.controller;

import com.example.demo.Service.ChatbotService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody ChatRequest request) {
        String response = chatbotService.chat(request.getSessionId(), request.getMessage());
        return ResponseEntity.ok(response);
    }

    @Data
    public static class ChatRequest {
        private String sessionId; // ID phiên hội thoại (client gửi để lưu context)
        private String message;   // Câu hỏi của người dùng
    }
}
