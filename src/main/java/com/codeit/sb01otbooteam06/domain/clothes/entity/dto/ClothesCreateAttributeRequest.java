package com.codeit.sb01otbooteam06.domain.clothes.entity.dto;

import java.util.UUID;

//의상 생성시 필요한 attributesDef dto
public record ClothesCreateAttributeRequest(
    UUID definitionId,
    String value
) {

}
