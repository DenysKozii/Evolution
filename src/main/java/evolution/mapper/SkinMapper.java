package evolution.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import evolution.dto.SkinDto;
import evolution.entity.Skin;

@Mapper(componentModel = "spring")
public interface SkinMapper {
    SkinMapper INSTANCE = Mappers.getMapper(SkinMapper.class);

    SkinDto mapToDto(Skin skin);
}
