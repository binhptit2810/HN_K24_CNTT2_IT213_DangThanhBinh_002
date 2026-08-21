package org.example.hackathon_de02.service;
import lombok.RequiredArgsConstructor;
import org.example.hackathon_de02.model.dto.TicketBookingItemRequest;
import org.example.hackathon_de02.model.dto.TicketBookingResult;
import org.example.hackathon_de02.model.entity.Viewer;
import org.example.hackathon_de02.repository.MovieRepository;
import org.example.hackathon_de02.repository.TicketBookingRepository;
import org.example.hackathon_de02.repository.TicketItemRepository;
import org.example.hackathon_de02.repository.ViewerRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketBookingService {

    private final ViewerRepository viewerRepository;
    private final MovieRepository movieRepository;
    private final TicketBookingRepository ticketBookingRepository;
    private final TicketItemRepository ticketItemRepository;

    @Tool(description = "Đặt vé xem phim. Tạo viewer theo số điện thoại nếu chưa có, kiểm tra đủ vé trước khi tạo, trừ stock đúng số lượng và trả về tổng tiền.")
    @Transactional
    public TicketBookingResult createTicketBooking(String viewerPhone, String viewerName, List<TicketBookingItemRequest> items) {
        if (viewerPhone == null || viewerPhone.isBlank()) {
            throw new IllegalArgumentException("viewerPhone là bắt buộc.");
        }
        if (viewerName == null || viewerName.isBlank()) {
            throw new IllegalArgumentException("viewerName là bắt buộc.");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Danh sách phim đặt vé không được để trống.");
        }

        Viewer viewer = viewerRepository.findByPhone(viewerPhone)
                .orElseGet(() -> viewerRepository.save(new Viewer(null, viewerName, viewerPhone, null, null)));

        if (!viewerName.equals(viewer.getFullName())) {
            viewer.setFullName(viewerName);
            viewer = viewerRepository.save(viewer);
        }





}
