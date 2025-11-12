package com.example.chatbot_manage_service.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyCount {
    private String date;  // yyyy-MM-dd
    private long count;
    private String dayOfWeek; // thêm field này
}
