package com.example.chatbot_manage_service.service;

import com.example.chatbot_manage_service.dto.request.ChatRequest;
import com.example.chatbot_manage_service.dto.response.ApiResponse;
import com.example.chatbot_manage_service.exception.ErrorCode;
import com.example.chatbot_manage_service.models.Conversation;
import com.example.chatbot_manage_service.models.Message;
import com.example.chatbot_manage_service.repositories.ConversationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ChatBotServiceImpl implements ChatBotManageService{
    private final ConversationRepository conversationRepository;
    private final ChatClient geminiClient;

    public ChatBotServiceImpl(ConversationRepository conversationRepository, ChatClient.Builder chatClientBuilder) {
        this.conversationRepository = conversationRepository;
        this.geminiClient = chatClientBuilder.build();
    }

    @Override
    public ApiResponse<List<Conversation>> getAllConversation() {
        return ApiResponse.<List<Conversation>>builder()
                .result(conversationRepository.findAll())
                .build();
    }

    @Override
    public ApiResponse<List<Conversation>> getDetailConversation(Integer userId) {
        return ApiResponse.<List<Conversation>>builder()
                .result( conversationRepository.findByUserId(userId))
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse<?>> deleteConversation(String id) {
        if (!conversationRepository.existsById(id)){
            return ResponseEntity
                    .status(ErrorCode.CONVERSATION_NOT_EXISTED.getHttpStatusCode())
                    .body(
                            ApiResponse.builder()
                                    .code(ErrorCode.CONVERSATION_NOT_EXISTED.getCode())
                                    .message(ErrorCode.CONVERSATION_NOT_EXISTED.getMessage())
                                    .build()
                    );
        }
        conversationRepository.deleteById(id);
        return ResponseEntity
                .ok()
                .body(
                        ApiResponse.builder()
                                .message("Delete successfully")
                                .build()
                );
    }

    @Override
    public String chat(ChatRequest request) {
        SystemMessage systemMessage = new SystemMessage("""
                You are Devteria.AI
                You should response with a formal voice
                """);

        UserMessage userMessage = new UserMessage(request.message());

        Prompt prompt = new Prompt(systemMessage, userMessage);

        return geminiClient
                .prompt(prompt)
                .call()
                .content();
    }

    @Override
//    public Conversation analyzeConversation(String conversationId) {
    public int analyzeConversation(String conversationId) {
        int analyzed = 2;
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException(ErrorCode.CONVERSATION_NOT_EXISTED.getMessage()));

        StringBuilder conversationText = new StringBuilder();
        if (conversation.getMessages() != null && !conversation.getMessages().isEmpty()) {
            for (Message msg : conversation.getMessages()) {
                conversationText
                        .append(msg.getSender())
                        .append(": ")
                        .append(msg.getContent())
                        .append("\n");
            }
        } else {
            conversationText.append("(Content is null)");
        }

        String promptText = """
                You are a system that analyzes customer intent through chat messages.  
                Your task is to classify the conversation into ONE of the following two categories:
                
                - 2: Potential — The user shows **any kind of intent or interest** in the store’s products or services.  
                    * Examples: Asking about prices, requesting product details, inquiring about how to make a purchase, asking for consultation, or checking order status.
                
                - 3: Non-Potential / Spam — **All other cases.**  
                    * Includes: Real spam messages (ads, suspicious links), casual greetings ("hello", "hi", "thank you"), irrelevant questions (asking about the weather, “who are you?”), or meaningless texts (random characters, emojis).
                
                ### Additional decision-making principles:
                
                * **“Ink Drop” Rule:** If there is even **ONE** message in the entire conversation that shows any interest (e.g., asking for price, product info, or consultation), the whole conversation must be classified as **2**.  
                * **Holistic Evaluation:** Always consider the **entire** conversation history. A chat that starts with “Hello” (which looks like type 3) but later includes “How much is this?” must be classified as **2**.  
                * **Focus on Intent:** Ignore spelling mistakes, abbreviations, or grammatical errors (e.g., “gia nhiu”, “cn hang k”). Focus only on the **actual intent** behind the words.  
                * **What is type 3:** Type **3** refers to “dead” conversations in a business sense — ones that do not lead to any consultation or sales-related action.
                
                In other words, ask yourself:  
                “Does this conversation show **any sign** that the user is interested in buying or learning about a product?”  
                - If YES → return **2**.  
                - If NO → return **3**.
                
                Below is the conversation content:
                """ + "\n\n" + conversationText + """
                
                Return ONLY a SINGLE NUMBER: 2 or 3.  
                Do NOT include any explanation or extra text.
                """;


        // 🧩 2️⃣ Gửi prompt đến Gemini
        SystemMessage systemMessage = new SystemMessage("You are classification conversation system.");
        UserMessage userMessage = new UserMessage(promptText);
        Prompt prompt = new Prompt(systemMessage, userMessage);

        String response = geminiClient
                .prompt(prompt)
                .call()
                .content()
                .trim();

        int status;
        if (response.equals("2")) {
            status = 2; // potential
        } else if (response.equals("3")) {
            status = 3; // spam
        } else {
            status = 1; // fallback (undefine)
        }

        // 📝 4️⃣ Cập nhật trạng thái hội thoại
//        conversation.setStatus(status);
//        conversation.setAnalyzed(analyzed); // đã phân tích
//        conversationRepository.save(conversation);

        return status;
    }
}

