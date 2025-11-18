package fr.moviefinder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record ActorDTO(
        Long id,

        @NotEmpty
        String firstname,

        @NotEmpty
        String lastname
) {}