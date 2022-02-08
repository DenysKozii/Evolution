package evolution.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import evolution.dto.FriendRequestDto;
import evolution.entity.FriendRequest;

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {
    FriendRequestMapper INSTANCE = Mappers.getMapper(FriendRequestMapper.class);

    FriendRequestDto mapToDto(FriendRequest friendRequest);
}
