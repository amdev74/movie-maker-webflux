package fr.moviefinder;

import fr.moviefinder.dto.ActorDTO;
import fr.moviefinder.dto.MovieDTO;
import fr.moviefinder.entity.Actor;
import fr.moviefinder.entity.Genre;
import fr.moviefinder.entity.Movie;
import fr.moviefinder.repository.ActorRepository;
import fr.moviefinder.repository.MovieActorRepository;
import fr.moviefinder.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
class MovieControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MovieActorRepository movieActorRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @BeforeEach
    void setUp() {
        movieActorRepository.deleteAll().block();
        movieRepository.deleteAll().block();
        actorRepository.deleteAll().block();
    }

    @Test
    @DisplayName("POST should create movie and return 201")
    void createMovie_ShouldReturn201WithMovieDTO() {
        // Given
        Actor actor = actorRepository.save(Actor.builder()
                .firstname("Mark").lastname("Hamill")
                .createdAt(LocalDateTime.now()).build()).block();

        MovieDTO request = MovieDTO.builder()
                .name("Star Wars IV")
                .genre(Genre.SCIENCE_FICTION)
                .publication(LocalDate.of(1977, 5, 25))
                .actors(List.of(ActorDTO.builder()
                        .id(actor.getId()).firstname("Mark").lastname("Hamill").build()))
                .build();

        // When & Then
        MovieDTO result;
        webTestClient
                .post()
                .uri("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(MovieDTO.class);
    }

    @Test
    @DisplayName("GET between dates should return movies")
    void getAllMoviesBetweenDate_ShouldReturnMovies() {
        // Given
        movieRepository.save(Movie.builder()
                .name("Avatar")
                .genre(Genre.SCIENCE_FICTION)
                .publication(LocalDate.of(2009, 12, 18))
                .createdAt(LocalDateTime.now()).build()).block();

        movieRepository.save(Movie.builder()
                .name("Inception")
                .genre(Genre.SCIENCE_FICTION)
                .publication(LocalDate.of(2010, 7, 16))
                .createdAt(LocalDateTime.now()).build()).block();

        movieRepository.save(Movie.builder()
                .name("La Ligne verte")
                .genre(Genre.DRAMA)
                .publication(LocalDate.of(1999, 1, 1))
                .createdAt(LocalDateTime.now()).build()).block();

        // When & Then
        webTestClient
                .get()
                .uri("/api/v1/movies?from=2009-01-01&to=2010-12-31")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovieDTO.class)
                .hasSize(2);
    }

    @Test
    @DisplayName("GET between dates with no results should return empty")
    void getAllMoviesBetweenDate_ShouldReturnEmpty() {
        // When & Then
        webTestClient
                .get()
                .uri("/api/v1/movies?from=2009-01-01&to=2010-12-31")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MovieDTO.class)
                .hasSize(0);
    }
}