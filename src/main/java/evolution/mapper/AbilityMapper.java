package evolution.mapper;

import evolution.dto.AbilityDto;
import evolution.entity.Ability;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AbilityMapper {
    AbilityMapper INSTANCE = Mappers.getMapper(AbilityMapper.class);

    AbilityDto mapToDto(Ability ability);
}
