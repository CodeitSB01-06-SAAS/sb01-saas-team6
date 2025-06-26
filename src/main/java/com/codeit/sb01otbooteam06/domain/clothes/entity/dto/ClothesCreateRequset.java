package com.codeit.sb01otbooteam06.domain.clothes.entity.dto;

import java.util.List;
import java.util.UUID;

public record ClothesCreateRequset(
    UUID ownerId,
    String name,
    String type,
    List<ClothesCreateAttributeRequest> attributes

) {

}
