package com.example.chatbot_manage_service.dto;

import lombok.Data;

@Data
public class DailyStat {
    private String date;
    private int count;
    private String dayOfWeek; // thêm field này
}

