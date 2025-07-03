package com.codeit.sb01otbooteam06.domain.clothes.mapper;

import com.codeit.sb01otbooteam06.domain.clothes.entity.Clothes;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClothesMapper {


  /**
   * Maps a {@link Clothes} entity to a {@link ClothesDto}, assigning the owner's ID to the {@code ownerId} field and omitting the {@code attributes} field.
   *
   * The {@code attributes} field in the resulting DTO will not be populated and should be set separately if needed.
   *
   * @param clothes the Clothes entity to map
   * @return the mapped ClothesDto with ownerId set and attributes ignored
   */
  @Mapping(source = "owner.id", target = "ownerId")
  @Mapping(target = "attributes", ignore = true)
    // true는 해당 필드 무시함. 추후 별도 주입
  ClothesDto toDto(Clothes clothes);
  

  /**
 * Converts a ClothesDto object to a Clothes entity.
 *
 * @param clothesDto the data transfer object representing clothing information
 * @return the corresponding Clothes entity
 */
Clothes toEntity(ClothesDto clothesDto);

}
