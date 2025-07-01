package com.codeit.sb01otbooteam06.domain.clothes.entity.dto;

import java.util.List;
import java.util.UUID;

//추천시 반환하는 의상 정보 dto
public record RecommendationClothesDto(
    UUID clothesId,
    String name,
    String imageUrl,
    String type,
    List<ClothesAttributeWithDefDto> clothesAttributeWithDefDtos
) {

}
