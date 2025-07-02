package com.codeit.sb01otbooteam06.domain.clothes.controller;

import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDefDto;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.PageResponse;
import com.codeit.sb01otbooteam06.domain.clothes.service.AttributeDefService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/clothes/attribute-defs")
public class AttributeDefController {

  private final AttributeDefService attributeDefService;

  public ResponseEntity<PageResponse<ClothesAttributeDefDto>> findAll(
      @RequestParam(value = "cursor", required = false) String cursor,
      @RequestParam(value = "idAfter", required = false) String idAfter,
      @RequestParam(value = "limit", defaultValue = "50") int limit,
      @RequestParam(value = "sortBy") String sortBy,
      @RequestParam(value = "sortDirection", defaultValue = "ASCENDING") String sortDirection,
      @RequestParam(value = "keywordLike", required = false) String keywordLike
  ) {
    PageResponse<ClothesAttributeDefDto> result = attributeDefService.findAll(cursor, idAfter,
        limit, sortBy, sortDirection, keywordLike);
    return ResponseEntity.ok(result);
  }

}
