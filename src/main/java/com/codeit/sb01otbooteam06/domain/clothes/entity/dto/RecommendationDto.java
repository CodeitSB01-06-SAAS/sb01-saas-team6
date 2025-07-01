package com.codeit.sb01otbooteam06.domain.clothes.entity.dto;

import java.util.List;

public record RecommendationDto(
    String weatherId,
    String userId,
    List<RecommendationClothesDto> clothes
) {

}
