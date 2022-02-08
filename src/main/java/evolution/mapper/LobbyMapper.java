package evolution.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import evolution.dto.LobbyDto;
import evolution.entity.Lobby;

@Mapper(componentModel = "spring")
public interface LobbyMapper {
    LobbyMapper INSTANCE = Mappers.getMapper(LobbyMapper.class);

    LobbyDto mapToDto(Lobby lobby);
}
