package evolution.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import evolution.dto.AbilityDto;
import evolution.entity.Ability;

@Mapper(componentModel = "spring")
public interface AbilityMapper {
    AbilityMapper INSTANCE = Mappers.getMapper(AbilityMapper.class);

    AbilityDto mapToDto(Ability ability);
}
