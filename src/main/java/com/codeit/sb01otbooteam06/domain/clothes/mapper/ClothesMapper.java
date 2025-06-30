package com.codeit.sb01otbooteam06.domain.clothes.mapper;

import com.codeit.sb01otbooteam06.domain.clothes.entity.Clothes;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClothesMapper {

  ClothesDto toDto(Clothes clothes);

  Clothes toEntity(ClothesDto clothesDto);

}
