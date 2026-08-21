package org.example.hackathon_de02.model.dto;

public record ChatRequest(
        String sessionId,
        String message
) {
}
