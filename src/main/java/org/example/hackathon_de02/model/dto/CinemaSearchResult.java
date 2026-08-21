package org.example.hackathon_de02.model.dto;

import java.util.Map;

public record CinemaSearchResult(
        String content,
        Map<String, Object> metadata
) {
}
