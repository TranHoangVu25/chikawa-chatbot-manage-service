package com.example.chatbot_manage_service.repositories;

import com.example.chatbot_manage_service.models.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<ConversationRepository,String> {
    Optional<Conversation> findByThreadId(String threadId);
}
