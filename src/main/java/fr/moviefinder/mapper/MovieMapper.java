package fr.moviefinder.mapper;

import fr.moviefinder.dto.MovieDTO;
import fr.moviefinder.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ActorMapper.class}
)
public interface MovieMapper {
    MovieDTO toDTO(Movie movie);

    @Mapping(target = "actors", ignore = true)
    Movie toEntity(MovieDTO movieDTO);
}
