package org.example.hackathon_de02.controller;

import lombok.RequiredArgsConstructor;
import org.example.hackathon_de02.model.dto.ChatRequest;
import org.example.hackathon_de02.model.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.context.annotation.Profile;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        if (request.sessionId() == null || request.sessionId().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "sessionId là bắt buộc.");
        }
        if (request.message() == null || request.message().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "message là bắt buộc.");
        }

        String reply = chatClient.prompt()
                .user(request.message())
                .advisors(advisor -> advisor.param("chat_memory_conversation_id", request.sessionId()))
                .call()
                .content();

        return ResponseEntity.ok(new ChatResponse(request.sessionId(), reply));
    }
}
