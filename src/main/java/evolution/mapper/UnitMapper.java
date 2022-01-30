package evolution.mapper;

import evolution.dto.UnitDto;
import evolution.entity.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UnitMapper {
    UnitMapper INSTANCE = Mappers.getMapper(UnitMapper.class);

    UnitDto mapToDto(Unit unit);
}
