package com.codeit.sb01otbooteam06.domain.clothes.mapper;


import com.codeit.sb01otbooteam06.domain.clothes.entity.AttributeDef;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDefDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttributeDefMapper {

  /**
 * Converts an {@link AttributeDef} entity to its corresponding {@link ClothesAttributeDefDto} data transfer object.
 *
 * @param attributeDef the attribute definition entity to convert
 * @return the mapped data transfer object representing the attribute definition
 */
ClothesAttributeDefDto toDto(AttributeDef attributeDef);
}
