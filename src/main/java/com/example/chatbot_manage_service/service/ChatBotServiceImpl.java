package com.example.chatbot_manage_service.service;

import com.example.chatbot_manage_service.dto.response.ApiResponse;
import com.example.chatbot_manage_service.exception.ErrorCode;
import com.example.chatbot_manage_service.models.Conversation;
import com.example.chatbot_manage_service.repositories.ConversationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatBotServiceImpl implements ChatBotManageService{
    ConversationRepository conversationRepository;

    @Override
    public ApiResponse<List<Conversation>> getAllConversation() {
        return ApiResponse.<List<Conversation>>builder()
                .result(conversationRepository.findAll())
                .build();
    }

    @Override
    public ApiResponse<List<Conversation>> getDetailConversation(Integer userId) {
        return ApiResponse.<List<Conversation>>builder()
                .result( conversationRepository.findByUserId(userId))
                .build();
    }

    @Override
    public Conversation analyze() {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> deleteConversation(String id) {
        if (!conversationRepository.existsById(id)){
            return ResponseEntity
                    .status(ErrorCode.CONVERSATION_NOT_EXISTED.getHttpStatusCode())
                    .body(
                            ApiResponse.builder()
                                    .code(ErrorCode.CONVERSATION_NOT_EXISTED.getCode())
                                    .message(ErrorCode.CONVERSATION_NOT_EXISTED.getMessage())
                                    .build()
                    );
        }
        conversationRepository.deleteById(id);
        return ResponseEntity
                .ok()
                .body(
                        ApiResponse.builder()
                                .message("Delete successfully")
                                .build()
                );
    }

}
