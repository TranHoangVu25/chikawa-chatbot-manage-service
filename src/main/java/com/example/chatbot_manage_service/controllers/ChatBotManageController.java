package com.example.chatbot_manage_service.controllers;

import com.example.chatbot_manage_service.dto.ConversationOverviewDTO;
import com.example.chatbot_manage_service.dto.ConversationStatsDTO;
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

    //get all conservation
    @GetMapping
    public ApiResponse<List<Conversation>> getAllConversation(){
        return ApiResponse.<List<Conversation>>builder()
                .result(chatBotManageService.getAllConversation())
                .build();
    }

    //get all conversation follow user id
    @GetMapping("/detail/{userId}")
    public ApiResponse<List<Conversation>> getDetailConversation(
            @PathVariable Integer userId
    ){
        return chatBotManageService.getDetailConversation(userId);
    }

    //remove conversation
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteConversation(
            @PathVariable String id
    ){
        return chatBotManageService.deleteConversation(id);
    }

    @PostMapping("/chat")
    String chat(@RequestBody ChatRequest request) {
        return chatBotManageService.chat(request);
    }

    //analyze conversation with id
    @PostMapping("/analyze/{id}")
    public ResponseEntity<ApiResponse<Conversation>> analyzeConversation(
            @PathVariable String id
    ) {
       return chatBotManageService.analyzeConversation(id);
    }

    //statistic conversation
    @GetMapping("/statistic")
    public ConversationStatsDTO getConversationStatistics(){
        return chatBotManageService.getConversationStatistics();
    }

    @GetMapping("/overview")
    public ApiResponse<ConversationOverviewDTO> getConversationOverview() {
        ConversationStatsDTO stats = chatBotManageService.getConversationStatistics();
        List<Conversation> conversations = chatBotManageService.getAllConversation();

        ConversationOverviewDTO overview = new ConversationOverviewDTO(stats, conversations);

        return ApiResponse.<ConversationOverviewDTO>builder()
                .result(overview)
                .build();
    }

    @GetMapping("/filter-status")
    public List<Conversation> filterStatus(
            @RequestParam(defaultValue = "0") Integer status
    ){
        return chatBotManageService.findByStatus(status);
    }
}
