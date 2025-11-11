package com.example.chatbot_manage_service.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Document(collection = "conversations")
    public class Conversation {
    @Id
    private String id;
    @Field("threadId")
    String threadId;
    @Field("userId")
    Integer userId;
        //1.agent
        //2.both
    @Field("agent_type")
    Integer agent_type;
    @Field("messages")
    List<Message> messages;
    @Field("createdAt")
    private Date createdAt = new Date();
}

