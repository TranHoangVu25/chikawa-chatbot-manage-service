package com.example.chatbot_manage_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    String sender;        // "user" hoặc "bot"
    String content;
    LocalDateTime timestamp;
}
