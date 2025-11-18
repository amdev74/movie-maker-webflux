package fr.moviefinder.service;

import fr.moviefinder.dto.MovieDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface MovieService {

    /**
     * Find a movie by name
     *
     * @param name
     * @return
     */
    Mono<MovieDTO> findByName(String name);

    /**
     * Find a list of movies between start and end date
     *
     * @param start lower limit of publication date
     * @param end   upper limot of publication date
     * @return a FLux of Movie
     */
    Flux<MovieDTO> findByPublicationDateBetween(LocalDate start, LocalDate end);

    /**
     * Find all movies
     *
     * @return a flux of Movie
     */
    Flux<MovieDTO> findAll();

    /**
     * Create a movie, and associated actors if they are missing
     * @param movie Representation of a movie with actors
     * @return a Mono of the movie created
     */
    Mono<MovieDTO> create(MovieDTO movie);
}
