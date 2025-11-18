package fr.moviefinder.mapper;

import fr.moviefinder.dto.ActorDTO;
import fr.moviefinder.entity.Actor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActorMapper {
    ActorDTO toDTO(Actor actor);

    Actor toEntity(ActorDTO actorDTO);
}
