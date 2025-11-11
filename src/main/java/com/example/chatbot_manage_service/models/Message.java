package com.example.chatbot_manage_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    @Field("sender")
    String sender;        // "user" hoặc "bot"
    @Field("content")
    String content;
    @Field("timestamp")
    private Date timestamp = new Date();
}
