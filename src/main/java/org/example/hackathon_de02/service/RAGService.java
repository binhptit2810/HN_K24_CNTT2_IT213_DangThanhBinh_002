package org.example.hackathon_de02.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.hackathon_de02.model.dto.CinemaSearchResult;
import org.example.hackathon_de02.model.dto.MovieLookupResult;
import org.example.hackathon_de02.model.entity.Movie;
import org.example.hackathon_de02.repository.MovieRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RAGService {

    private final MovieRepository movieRepository;
    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void ingestCinemaInfoIfNeeded() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM vector_store", Integer.class);
            if (count != null && count > 0) {
                log.info("Vector store already contains {} rows; skip cinema PDF ingestion.", count);
                return;
            }

            List<Document> documents = new TikaDocumentReader(
                    new ClassPathResource("De02_CinemaStar_ThongTin.pdf")
            ).get();
            if (!documents.isEmpty()) {
                vectorStore.add(documents);
                log.info("Ingested {} cinema documents into vector store.", documents.size());
            }
        } catch (Exception ex) {
            log.warn("Skipping cinema PDF ingestion because vector store bootstrap failed.", ex);
        }
    }

    @Tool(description = "Tra cứu phim theo tên và trả về đầy đủ thông tin, bao gồm số vé còn lại.")
    @Transactional(readOnly = true)
    public List<MovieLookupResult> searchMovieByName(String movieName) {
        validateQuery(movieName, "movieName");
        return movieRepository.findByNameContainingIgnoreCaseOrderByNameAsc(movieName)
                .stream()
                .map(this::toMovieLookupResult)
                .toList();
    }

    @Tool(description = "Tra cứu phim theo thể loại và trả về đầy đủ thông tin, bao gồm số vé còn lại.")
    @Transactional(readOnly = true)
    public List<MovieLookupResult> searchMovieByGenre(String genre) {
        validateQuery(genre, "genre");
        return movieRepository.findByGenre_NameContainingIgnoreCaseOrderByNameAsc(genre)
                .stream()
                .map(this::toMovieLookupResult)
                .toList();
    }

    @Tool(description = "Tra cứu thông tin rạp chiếu phim từ kho vector_store và trả về các đoạn liên quan nhất.")
    public List<CinemaSearchResult> getCinemaInfo(String question) {
        validateQuery(question, "question");
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder().query(question).topK(3).build()
        );

        return documents.stream()
                .map(document -> new CinemaSearchResult(
                        document.getFormattedContent(),
                        document.getMetadata() == null ? Map.of() : document.getMetadata()
                ))
                .toList();
    }

    private void validateQuery(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " là bắt buộc.");
        }
    }

    private MovieLookupResult toMovieLookupResult(Movie movie) {
        return new MovieLookupResult(
                movie.getId(),
                movie.getName(),
                movie.getGenre() == null ? null : movie.getGenre().getName(),
                movie.getDescription(),
                movie.getPrice(),
                movie.getStock()
        );
    }

}
