package com.parampara.bazaar.bargain.chatbot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parampara.bazaar.bargain.dto.ChatRequest;
import com.parampara.bazaar.bargain.dto.ChatResponse;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/{sessionId}/chat")
    public ResponseEntity<ChatResponse> chat(
            @PathVariable String sessionId,
            @Valid @RequestBody ChatRequest req) {
        ChatResponse res = new ChatResponse();
        res.setReply(
                "This is the backend responding! You said: " + req.getMessage() + ". We are successfully connected.");
        res.setShopkeeperOffer(req.getBuyerOffer() != null ? req.getBuyerOffer() + 50 : 1000.0);
        res.setStatus("NEGOTIATING");
        res.setSuccess(true);
        return ResponseEntity.ok(res);
    }
}