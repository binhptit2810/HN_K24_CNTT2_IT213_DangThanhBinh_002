package org.example.hackathon_de02.model.dto;

import java.util.List;

public record TicketBookingRequest(
        String viewerPhone,
        String viewerName,
        List<TicketBookingItemRequest> items
) {
}
