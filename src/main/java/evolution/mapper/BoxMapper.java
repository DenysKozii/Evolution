package evolution.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import evolution.dto.BoxDto;
import evolution.dto.UnitDto;
import evolution.entity.Box;
import evolution.entity.Unit;

@Mapper(componentModel = "spring")
public interface BoxMapper {
    BoxMapper INSTANCE = Mappers.getMapper(BoxMapper.class);

    BoxDto mapToDto(Box box);
}
