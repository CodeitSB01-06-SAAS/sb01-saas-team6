package com.codeit.sb01otbooteam06.domain.clothes.controller;

import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDefCreateRequest;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDefDto;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDefUpdateRequest;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.PageResponse;
import com.codeit.sb01otbooteam06.domain.clothes.service.AttributeDefService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/clothes/attribute-defs")
public class AttributeDefController {

  private final AttributeDefService attributeDefService;

  /**
   * Creates a new clothes attribute definition.
   *
   * @param request the details of the clothes attribute definition to create
   * @return a response containing the created clothes attribute definition
   */
  @PostMapping
  public ResponseEntity<ClothesAttributeDefDto> create(
      @RequestBody ClothesAttributeDefCreateRequest request) {

    ClothesAttributeDefDto clothesAttributeDefDto = attributeDefService.create(request);
    return ResponseEntity.ok(clothesAttributeDefDto);

  }

  /**
   * Retrieves a paginated list of clothes attribute definitions with optional filtering and sorting.
   *
   * @param cursor        an optional pagination cursor for fetching the next page of results
   * @param idAfter       an optional ID to fetch results after a specific attribute definition
   * @param limit         the maximum number of results to return (default is 50)
   * @param sortBy        the field by which to sort the results
   * @param sortDirection the direction of sorting, either "ASCENDING" or "DESCENDING" (default is "ASCENDING")
   * @param keywordLike   an optional keyword for filtering attribute definitions by name or description
   * @return a ResponseEntity containing a paginated response of clothes attribute definitions
   */
  @GetMapping
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

  /**
   * Updates an existing clothes attribute definition with the specified ID.
   *
   * @param definitionId the UUID of the attribute definition to update
   * @param updateRequest the update data for the attribute definition
   * @return a ResponseEntity containing the updated ClothesAttributeDefDto
   */
  @PatchMapping("/{definitionId}")
  public ResponseEntity<ClothesAttributeDefDto> update(
      @PathVariable UUID definitionId,
      @RequestBody ClothesAttributeDefUpdateRequest updateRequest
  ) {

    ClothesAttributeDefDto clothesAttributeDefDto = attributeDefService.update(definitionId,
        updateRequest);

    return ResponseEntity.ok(clothesAttributeDefDto);
  }

  /**
   * Deletes a clothes attribute definition identified by the given UUID.
   *
   * @param definitionId the UUID of the attribute definition to delete
   * @return a response entity with HTTP 204 No Content if deletion is successful
   */
  @DeleteMapping("/{definitionId}")
  public ResponseEntity<Void> delete(
      @PathVariable(value = "definitionId") UUID definitionId) {

    attributeDefService.delete(definitionId);
    return ResponseEntity.noContent().build();

  }

}
