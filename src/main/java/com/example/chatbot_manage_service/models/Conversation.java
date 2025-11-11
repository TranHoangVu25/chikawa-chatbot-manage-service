package com.example.chatbot_manage_service.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    Integer agentType;
    @Field("messages")
    List<Message> messages;
    @Field("status") // 1 = undefine, 2 = potential, 3 = spam
    Integer status;
    @Field("analyzed") // 1 = pending, 2 = analyzed
    Integer analyzed;
    @Field("role") // 1 = guest, 2 = customer
    Integer role;
    @Field("createdAt")
    private Date createdAt = new Date();
}

