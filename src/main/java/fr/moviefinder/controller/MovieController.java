package fr.moviefinder.controller;

import fr.moviefinder.dto.MovieDTO;
import fr.moviefinder.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovieDTO> getAllMovies() {
        return movieService.findAll();
    }

    @GetMapping(params = {"from", "to"})
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovieDTO> getAllMoviesBetweenDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return movieService.findByPublicationDateBetween(from,to);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/{name}")
    public Mono<MovieDTO> getMovieByName(@PathVariable String name) {
        return movieService.findByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movie) {
        return movieService.create(movie);
    }
}
