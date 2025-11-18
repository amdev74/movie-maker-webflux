package fr.moviefinder.repository;

import fr.moviefinder.entity.Movie;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface MovieRepository extends ReactiveCrudRepository<Movie, Long> {
    @Query("""
            SELECT * FROM movie
            WHERE name = :name
            """)
    Flux<Movie> findByName(String name);


    @Query("""
            SELECT * FROM movie
            WHERE publication_date BETWEEN :start AND :end
            ORDER BY publication_date DESC
            """)
    Flux<Movie> findByPublicationDateBetween(LocalDate start, LocalDate end);
}
