package org.example.hackathon_de02.repository;

import org.example.hackathon_de02.model.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByNameIgnoreCase(String name);

    List<Movie> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    List<Movie> findByGenre_NameContainingIgnoreCaseOrderByNameAsc(String genreName);
}
