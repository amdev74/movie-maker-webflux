package fr.moviefinder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.moviefinder.entity.Genre;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record MovieDTO(
        Long id,

        @NotEmpty
        String name,

        @NotNull
        Genre genre,

        @NotNull
        LocalDate publication,

        @NotEmpty(message = "At least one actor is required.")
        @Valid
        List<ActorDTO> actors
) {
    public MovieDTO {
        if (actors == null) {
            actors = new ArrayList<>();
        }
    }
}
