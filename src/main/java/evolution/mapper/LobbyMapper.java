package evolution.mapper;

import evolution.dto.LobbyDto;
import evolution.entity.Lobby;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LobbyMapper {
    LobbyMapper INSTANCE = Mappers.getMapper(LobbyMapper.class);

    LobbyDto mapToDto(Lobby lobby);
}
