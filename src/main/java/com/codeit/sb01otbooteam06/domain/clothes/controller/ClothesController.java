package com.codeit.sb01otbooteam06.domain.clothes.controller;

import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesCreateRequset;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesDto;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesUpdateRequest;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.PageResponse;
import com.codeit.sb01otbooteam06.domain.clothes.service.ClothesService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clothes")
public class ClothesController {

  private final ClothesService clothesService;

  /**
   * Creates a new clothes entry with the provided details and an optional image.
   *
   * @param clothesCreateRequset the request data for creating a clothes entry
   * @param file an optional image file associated with the clothes entry
   * @return the created clothes entry as a {@link ClothesDto} wrapped in an HTTP 200 OK response
   */
  @PostMapping
  public ResponseEntity<ClothesDto> create(
      @RequestPart(value = "request")
      ClothesCreateRequset clothesCreateRequset,
      @RequestPart(value = "image", required = false) MultipartFile file) {
    ClothesDto clothesDto = clothesService.create(clothesCreateRequset, file);
    return ResponseEntity.ok(clothesDto);
  }

  /**
   * Retrieves a paginated list of clothes filtered by optional cursor, idAfter, limit, type, and required owner ID.
   *
   * @param cursor    an optional pagination cursor indicating the starting point for the next page
   * @param idAfter   an optional clothes ID to fetch items after this ID
   * @param limit     the maximum number of items to return (default is 20)
   * @param typeEqual the type of clothes to filter by (default is "TOP")
   * @param ownerId   the UUID of the clothes owner (required)
   * @return a ResponseEntity containing a paginated response of ClothesDto objects
   */
  @GetMapping
  public ResponseEntity<PageResponse<ClothesDto>> findAll(
      @RequestParam(value = "cursor", required = false) String cursor,
      @RequestParam(value = "idAfter", required = false) String idAfter,
      @RequestParam(value = "limit", defaultValue = "20") int limit,
      @RequestParam(value = "typeEqual", defaultValue = "TOP") String typeEqual,
      @RequestParam(value = "ownerId") UUID ownerId
  ) {
    PageResponse<ClothesDto> result = clothesService.findAll(cursor, idAfter, limit, typeEqual,
        ownerId);
    return ResponseEntity.ok(result);

  }

  /**
   * Updates an existing clothes entry with new data and an optional image.
   *
   * @param clothesId the unique identifier of the clothes entry to update
   * @param clothesUpdateRequest the data for updating the clothes entry
   * @param file an optional image file to associate with the clothes entry
   * @return the updated clothes entry wrapped in an HTTP 200 OK response
   */
  @PatchMapping("/{clothesId}")
  public ResponseEntity<ClothesDto> update(@PathVariable("clothesId") UUID clothesId,
      @RequestPart("request") ClothesUpdateRequest clothesUpdateRequest,
      @RequestPart(value = "image", required = false) MultipartFile file) {
    ClothesDto clothesDto = clothesService.update(clothesId, clothesUpdateRequest, file);
    return ResponseEntity.ok(clothesDto);
  }

  /**
   * Deletes the clothes entity identified by the given ID.
   *
   * @param clothesId the unique identifier of the clothes to delete
   * @return a response with HTTP 204 No Content status if deletion is successful
   */
  @DeleteMapping("/{clothesId}")
  public ResponseEntity<Void> delete(@PathVariable("clothesId") UUID clothesId) {
    clothesService.delete(clothesId);
    return ResponseEntity.noContent().build();

  }

}
