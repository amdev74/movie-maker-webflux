package fr.moviefinder.repository;

import fr.moviefinder.entity.MovieActor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MovieActorRepository extends ReactiveCrudRepository<MovieActor, Long> {
}
