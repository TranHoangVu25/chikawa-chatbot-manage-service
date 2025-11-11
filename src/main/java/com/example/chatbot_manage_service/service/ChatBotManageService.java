package com.example.chatbot_manage_service.service;

import com.example.chatbot_manage_service.dto.request.ChatRequest;
import com.example.chatbot_manage_service.dto.response.ApiResponse;
import com.example.chatbot_manage_service.models.Conversation;
import com.example.chatbot_manage_service.repositories.ConversationRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatBotManageService {
    //hiển thị tất cả các cuộc hội thoại
    //scenario: ấn vào xem chi tiết thì hiển thị ra cuộc trò chuyện đó
    ApiResponse<List<Conversation>> getAllConversation();

/**
    scenario: trong khi xem chi tiết cuộc hội thoại có thể xem tất cả các
    cuộc hội thoại theo user id
**/
    ApiResponse<List<Conversation>> getDetailConversation(Integer userId);

    Conversation analyze();

    //xóa theo id conversation
    ResponseEntity<ApiResponse<?>>  deleteConversation(String id);

    String chat(ChatRequest request);
}
