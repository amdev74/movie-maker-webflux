package fr.moviefinder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("movie_actor")
public class MovieActor {
    @Column("movie_id")
    private Long movieId;

    @Column("actor_id")
    private Long actorId;
}
