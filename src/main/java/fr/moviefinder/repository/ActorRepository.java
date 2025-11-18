package fr.moviefinder.repository;

import fr.moviefinder.entity.Actor;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ActorRepository extends ReactiveCrudRepository<Actor, Long> {

    @Query("""
    SELECT a.* FROM actor a
    INNER JOIN movie_actor ma ON a.id = ma.actor_id
    WHERE ma.movie_id = :movieId
    ORDER BY a.lastname, a.firstname
    """)
    Flux<Actor> findByMovieId(Long movieId);
}
