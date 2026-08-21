package org.example.hackathon_de02.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hackathon_de02.model.entity.*;
import org.example.hackathon_de02.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializeService {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final ViewerRepository viewerRepository;

    @PostConstruct
    public void initializeDatabase() {
        if (genreRepository.count() == 0) {
            log.info("Initializing cinema demo data...");
            Genre action = genreRepository.save(new Genre(null, "Hành động", "Phim nhịp độ nhanh, nhiều cảnh gay cấn"));
            Genre comedy = genreRepository.save(new Genre(null, "Hài", "Phim giải trí, nhẹ nhàng"));
            Genre family = genreRepository.save(new Genre(null, "Gia đình", "Phim phù hợp nhiều lứa tuổi"));
            Genre sciFi = genreRepository.save(new Genre(null, "Khoa học viễn tưởng", "Phim về công nghệ và thế giới tương lai"));

            movieRepository.saveAll(List.of(
                new Movie(null, "Cuộc Đua Đêm", "Một bộ phim hành động về cuộc truy đuổi tốc độ cao.", new BigDecimal("120000"), 50, null, action),
                new Movie(null, "Cười Xuyên Màn Ảnh", "Phim hài nhẹ nhàng, vui vẻ cho cả nhóm bạn.", new BigDecimal("90000"), 40, null, comedy),
                new Movie(null, "Ngôi Nhà Bên Biển", "Câu chuyện gia đình ấm áp và cảm động.", new BigDecimal("110000"), 35, null, family),
                new Movie(null, "Vùng Trời Tương Lai", "Một chuyến phiêu lưu khoa học viễn tưởng đầy bất ngờ.", new BigDecimal("130000"), 25, null, sciFi)
            ));
            
            viewerRepository.saveAll(List.of(
                new Viewer(null, "User A", "0901234567", "a@example.com", "Address A"),
                new Viewer(null, "User B", "0912345678", "b@example.com", "Address B")
            ));
        }
    }
}
