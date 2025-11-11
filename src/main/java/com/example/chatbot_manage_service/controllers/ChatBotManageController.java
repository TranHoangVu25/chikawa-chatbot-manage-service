package com.example.chatbot_manage_service.controllers;

import com.example.chatbot_manage_service.dto.request.ChatRequest;
import com.example.chatbot_manage_service.dto.response.ApiResponse;
import com.example.chatbot_manage_service.models.Conversation;
import com.example.chatbot_manage_service.service.ChatBotManageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ChatBotManageController {
    ChatBotManageService chatBotManageService;
    ChatBotManageService chatService;

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
        return chatService.chat(request);
    }
}
