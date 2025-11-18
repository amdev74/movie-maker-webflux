package fr.moviefinder.service.impl;

import fr.moviefinder.dto.ActorDTO;
import fr.moviefinder.dto.MovieDTO;
import fr.moviefinder.entity.Actor;
import fr.moviefinder.entity.Movie;
import fr.moviefinder.entity.MovieActor;
import fr.moviefinder.exception.ResourceNotFoundException;
import fr.moviefinder.exception.ServiceException;
import fr.moviefinder.mapper.ActorMapper;
import fr.moviefinder.mapper.MovieMapper;
import fr.moviefinder.repository.ActorRepository;
import fr.moviefinder.repository.MovieActorRepository;
import fr.moviefinder.repository.MovieRepository;
import fr.moviefinder.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final MovieActorRepository movieActorRepository;
    private final MovieMapper movieMapper;
    private final ActorMapper actorMapper;

    @Override
    @Transactional(readOnly = true)
    public Mono<MovieDTO> findByName(String name) {
        return movieRepository
                .findByName(name)
                .collectList()
                .flatMap(movies -> {
                    if(movies.isEmpty()) {
                        return Mono.error(new ResourceNotFoundException());
                    }

                    if(movies.size() > 1) {
                        return Mono.error(new ServiceException("Multiple movies with same name."));
                    }

                    return Mono.just(movies.getFirst());
                })
                .flatMap(this::enrichMovieWithActors)
                .map(movieMapper::toDTO);
    }

    private Mono<Movie> enrichMovieWithActors(Movie movie) {
        return actorRepository.findByMovieId(movie.getId())
                .collectList()
                .map(actors -> {
                    movie.setActors(actors);
                    return movie;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MovieDTO> findByPublicationDateBetween(LocalDate start, LocalDate end) {
        return movieRepository.findByPublicationDateBetween(start, end).map(movieMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MovieDTO> findAll() {
        return movieRepository.findAll().map(movieMapper::toDTO);
    }

    @Override
    @Transactional
    public Mono<MovieDTO> create(MovieDTO movie) {
        return getOrCreateActors(movie.actors())
                .collectList()
                .flatMap(actors -> {
                    LocalDateTime now = LocalDateTime.now();
                    Movie movieToSave = movieMapper.toEntity(movie);
                    movieToSave.setCreatedAt(now);
                    movieToSave.setUpdatedAt(now);
                    
                    return movieRepository.save(movieToSave)
                            .flatMap(movieSaved ->
                                    associateActorsWithMovie(movieSaved, actors)
                                            .thenReturn(movieSaved))
                            .map(movieSaved -> {
                                movieSaved.setActors(actors);
                                return movieSaved;
                            })
                            .map(movieMapper::toDTO);
                });
    }

    private Mono<Void> associateActorsWithMovie(Movie movieSaved, List<Actor> actors) {
        return Flux.fromIterable(actors)
                .flatMap(actor ->
                    movieActorRepository.save(MovieActor
                            .builder()
                            .actorId(actor.getId())
                            .movieId(movieSaved.getId())
                            .build())
                )
                .then();
    }

    private Flux<Actor> getOrCreateActors(List<ActorDTO> actors) {
        return Flux.fromIterable(actors)
                .flatMap(actor -> actorRepository.findById(actor.id())
                        .switchIfEmpty(actorRepository.save(Actor.builder().firstname(actor.firstname()).lastname(actor.lastname()).build())));
    }
}
