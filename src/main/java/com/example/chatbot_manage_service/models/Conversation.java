package com.example.chatbot_manage_service.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Document("conversations")
    public class Conversation {
        @Id
        String threadId;
        Integer userId;

        //1.admin
        //2.agent
        //3.both
        Integer agent_type;
        List<Message> messages;
        LocalDateTime createdAt = LocalDateTime.now();
    }

