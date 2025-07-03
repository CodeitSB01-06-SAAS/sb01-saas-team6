package com.codeit.sb01otbooteam06.domain.clothes.mapper;


import com.codeit.sb01otbooteam06.domain.clothes.entity.ClothesAttribute;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClothesAttributeMapper {

  /**
 * Converts a {@link ClothesAttribute} entity to its corresponding {@link ClothesAttributeDto}.
 *
 * @param clothesAttribute the entity to convert
 * @return the mapped data transfer object representing the clothes attribute
 */
ClothesAttributeDto toDto(ClothesAttribute clothesAttribute);
}
