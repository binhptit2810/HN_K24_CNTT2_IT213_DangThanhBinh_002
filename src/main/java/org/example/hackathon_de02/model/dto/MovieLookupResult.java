package org.example.hackathon_de02.model.dto;

import java.math.BigDecimal;

public record MovieLookupResult(
        Long id,
        String movieName,
        String genreName,
        String description,
        BigDecimal price,
        Integer remainingTickets
) {
}
