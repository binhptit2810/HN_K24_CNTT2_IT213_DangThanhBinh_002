package org.example.hackathon_de02.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record TicketBookingResult(
        Long bookingId,
        String viewerPhone,
        String viewerName,
        String status,
        BigDecimal totalAmount,
        String note,
        List<TicketBookingItemResult> items
) {
}
