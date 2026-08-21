package org.example.hackathon_de02.model.dto;

import java.math.BigDecimal;

public record TicketBookingItemResult(
        String movieName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal,
        Integer remainingTickets
) {
}
