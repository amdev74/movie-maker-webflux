package fr.moviefinder;

import fr.moviefinder.dto.ActorDTO;
import fr.moviefinder.dto.MovieDTO;
import fr.moviefinder.entity.Actor;
import fr.moviefinder.entity.Genre;
import fr.moviefinder.repository.ActorRepository;
import fr.moviefinder.repository.MovieActorRepository;
import fr.moviefinder.repository.MovieRepository;
import fr.moviefinder.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MovieActorRepository movieActorRepository;

    @BeforeEach
    void setUp() {
        movieActorRepository.deleteAll().block();
        movieRepository.deleteAll().block();
        actorRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Should create movie with existing actors")
    void create_ShouldCreateMovieWithActors() {
        // Given
        Actor actor1 = actorRepository.save(Actor.builder()
                .firstname("Mark").lastname("hamill").createdAt(LocalDateTime.now()).build()).block();
        Actor actor2 = actorRepository.save(Actor.builder()
                .firstname("Harrison").lastname("Ford").createdAt(LocalDateTime.now()).build()).block();

        MovieDTO request = MovieDTO
                .builder()
                .name("Star Wars IV")
                .genre(Genre.SCIENCE_FICTION)
                .publication(LocalDate.of(1977, 5, 25))
                .actors(List.of(
                        ActorDTO.builder().id(actor1.getId()).build(),
                        ActorDTO.builder().id(actor2.getId()).build()
                ))
                .build();

        // When
        MovieDTO result = movieService.create(request).block();

        log.info(String.valueOf(result));

        // Then
        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo("Star Wars IV");
        assertThat(result.actors()).hasSize(2);
    }

    @Test
    @DisplayName("Find movie by name")
    void findByName() {
        // Given
        Actor actor1 = actorRepository.save(Actor.builder()
                .firstname("Mark").lastname("hamill").createdAt(LocalDateTime.now()).build()).block();
        Actor actor2 = actorRepository.save(Actor.builder()
                .firstname("Harrison").lastname("Ford").createdAt(LocalDateTime.now()).build()).block();

        MovieDTO request = MovieDTO.builder()
                .name("Star Wars IV")
                .genre(Genre.SCIENCE_FICTION)
                .publication(LocalDate.of(1977, 5, 25))
                .actors(List.of(
                        ActorDTO.builder().id(actor1.getId()).build(),
                        ActorDTO.builder().id(actor2.getId()).build()
                ))
                .build();

        movieService.create(request).block();

        // When
        MovieDTO result = movieService.findByName("Star Wars IV").block();

        log.info(String.valueOf(result));

        // Then
        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo("Star Wars IV");
        assertThat(result.actors()).hasSize(2);
    }
}