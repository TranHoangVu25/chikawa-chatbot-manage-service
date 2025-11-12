package com.example.chatbot_manage_service.dto;

import com.example.chatbot_manage_service.utils.DailyCount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ConversationStatsDTO {
    private long totalConversations;
    private long analyzedCount;
    private long pendingCount;
    private long potentialCount;
    private long spamCount;
    private List<DailyCount> dailyStats;
}
