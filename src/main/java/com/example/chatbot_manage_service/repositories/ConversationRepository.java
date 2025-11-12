package com.example.chatbot_manage_service.repositories;

import com.example.chatbot_manage_service.dto.ConversationStatsDTO;
import com.example.chatbot_manage_service.models.Conversation;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation,String> {
    Optional<Conversation> findByThreadId(String threadId);

    List<Conversation> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);

    void deleteByUserId(Integer userId);

    @Aggregation(pipeline = {
            "{ $match: { " +
                    "$expr: { " +
                    "$gte: [ { $toDate: '$createdAt' }, { $dateSubtract: { startDate: '$$NOW', unit: 'day', amount: 7 } } ] " +
                    "} " +
                    "} }",
            "{ $facet: { " +
                    "summary: [ " +
                    "{ $group: { " +
                    "_id: null, " +
                    "totalConversations: { $sum: 1 }, " +
                    "analyzedCount: { $sum: { $cond: [{ $eq: ['$analyzed', 2] }, 1, 0] } }, " +
                    "pendingCount: { $sum: { $cond: [{ $eq: ['$analyzed', 1] }, 1, 0] } }, " +
                    "potentialCount: { $sum: { $cond: [{ $eq: ['$status', 2] }, 1, 0] } }, " +
                    "spamCount: { $sum: { $cond: [{ $eq: ['$status', 3] }, 1, 0] } } " +
                    "} } " +
                    "], " +
                    "dailyStats: [ " +
                    "{ $group: { " +
                    "_id: { $dateToString: { format: '%Y-%m-%d', date: { $toDate: '$createdAt' } } }, " +
                    "count: { $sum: 1 } " +
                    "} }, " +
                    "{ $sort: { '_id': 1 } } " +
                    "] " +
                    "} }",
            "{ $project: { " +
                    "totalConversations: { $ifNull: [{ $arrayElemAt: ['$summary.totalConversations', 0] }, 0] }, " +
                    "analyzedCount: { $ifNull: [{ $arrayElemAt: ['$summary.analyzedCount', 0] }, 0] }, " +
                    "pendingCount: { $ifNull: [{ $arrayElemAt: ['$summary.pendingCount', 0] }, 0] }, " +
                    "potentialCount: { $ifNull: [{ $arrayElemAt: ['$summary.potentialCount', 0] }, 0] }, " +
                    "spamCount: { $ifNull: [{ $arrayElemAt: ['$summary.spamCount', 0] }, 0] }, " +
                    "dailyStats: { " +
                    "$map: { " +
                    "input: '$dailyStats', " +
                    "as: 'd', " +
                    "in: { date: '$$d._id', count: '$$d.count' } " +
                    "} " +
                    "} " +
                    "} }"
    })
    ConversationStatsDTO getConversationStatistics();

    List<Conversation> findByStatus(int status);
}
