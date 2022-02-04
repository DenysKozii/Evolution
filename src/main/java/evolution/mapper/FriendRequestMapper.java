package evolution.mapper;

import evolution.dto.FriendRequestDto;
import evolution.dto.UserDto;
import evolution.entity.FriendRequest;
import evolution.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {
    FriendRequestMapper INSTANCE = Mappers.getMapper(FriendRequestMapper.class);

    FriendRequestDto mapToDto(FriendRequest friendRequest);
}
