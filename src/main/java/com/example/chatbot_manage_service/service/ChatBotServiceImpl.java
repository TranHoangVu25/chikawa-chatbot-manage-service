package com.example.chatbot_manage_service.service;

import com.example.chatbot_manage_service.dto.request.ChatRequest;
import com.example.chatbot_manage_service.dto.response.ApiResponse;
import com.example.chatbot_manage_service.exception.ErrorCode;
import com.example.chatbot_manage_service.models.Conversation;
import com.example.chatbot_manage_service.repositories.ConversationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ChatBotServiceImpl implements ChatBotManageService{
    private final ConversationRepository conversationRepository;
    private final ChatClient chatClient;

    public ChatBotServiceImpl(ConversationRepository conversationRepository, ChatClient.Builder chatClientBuilder) {
        this.conversationRepository = conversationRepository;
        this.chatClient = chatClientBuilder.build();
    }

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

    @Override
    public Conversation analyze() {
        return null;
    }

    @Override
    public String chat(ChatRequest request) {
        SystemMessage systemMessage = new SystemMessage("""
                You are Devteria.AI
                You should response with a formal voice
                """);

        UserMessage userMessage = new UserMessage(request.message());

        Prompt prompt = new Prompt(systemMessage, userMessage);

        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }
}
