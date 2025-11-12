package com.example.chatbot_manage_service.dto;

import com.example.chatbot_manage_service.models.Conversation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationOverviewDTO {
    private ConversationStatsDTO stats;
    private List<Conversation> conversations;
}
