package com.example.chatbot_manage_service.controllers;

import com.example.chatbot_manage_service.dto.request.ChatRequest;
import com.example.chatbot_manage_service.dto.response.ApiResponse;
import com.example.chatbot_manage_service.models.Conversation;
import com.example.chatbot_manage_service.repositories.ConversationRepository;
import com.example.chatbot_manage_service.service.ChatBotManageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ChatBotManageController {
    ChatBotManageService chatBotManageService;
    ConversationRepository conversationRepository;

    @GetMapping
    public ApiResponse<List<Conversation>> getAllConversation(){
        return chatBotManageService.getAllConversation();
    }

    @GetMapping("/detail/{userId}")
    public ApiResponse<List<Conversation>> getDetailConversation(
            @PathVariable Integer userId
    ){
        return chatBotManageService.getDetailConversation(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteConversation(
            @PathVariable String id
    ){
        return chatBotManageService.deleteConversation(id);
    }

    @PostMapping("/chat")
    String chat(@RequestBody ChatRequest request) {
        return chatBotManageService.chat(request);
    }

    @PostMapping("/analyze/{id}")
    public ResponseEntity<?> analyzeConversation(@PathVariable String id) {
        // 1️⃣ Kiểm tra tồn tại conversation
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        // 2️⃣ Gọi service để phân tích hội thoại (tự lưu DB bên trong)
        int status = chatBotManageService.analyzeConversation(id);

        // 3️⃣ Lấy lại dữ liệu sau khi cập nhật
        conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found after analysis"));

        // 4️⃣ Trả về kết quả
        return ResponseEntity.ok(Map.of(
                "conversationId", conversation.getId(),
                "status", status,
                "analyzed", conversation.getAnalyzed(),
                "message", (status == 2 ? "potential" : status == 3 ? "spam" : "undefined")
        ));
    }
}
