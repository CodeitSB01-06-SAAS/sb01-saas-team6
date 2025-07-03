package com.codeit.sb01otbooteam06.domain.clothes.service;

import com.codeit.sb01otbooteam06.domain.clothes.entity.AttributeDef;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDefCreateRequest;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDefDto;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDefUpdateRequest;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.PageResponse;
import com.codeit.sb01otbooteam06.domain.clothes.exception.AttributeDefAlreadyExistsException;
import com.codeit.sb01otbooteam06.domain.clothes.exception.AttributeDefNotFoundException;
import com.codeit.sb01otbooteam06.domain.clothes.mapper.AttributeDefMapper;
import com.codeit.sb01otbooteam06.domain.clothes.repository.AttributeDefRepository;
import com.codeit.sb01otbooteam06.domain.clothes.repository.ClothesAttributeRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttributeDefService {

  /// 의상 속성 서비스

  //TODO: null 검증

  private final AttributeDefRepository attributeDefRepository;
  private final ClothesAttributeRepository attributeDefClothesRepository;

  private final AttributeDefMapper attributeDefMapper;


  /**
   * Creates a new clothing attribute definition if the name is unique.
   *
   * @param clothesAttributeDefCreateRequest the request containing the name and selectable values for the new attribute definition
   * @return the DTO representation of the created attribute definition
   * @throws AttributeDefAlreadyExistsException if an attribute definition with the given name already exists
   */
  @Transactional
  public ClothesAttributeDefDto create(
      ClothesAttributeDefCreateRequest clothesAttributeDefCreateRequest) {

    //중복 의상 속성 네임 검증
    if (attributeDefRepository.existsByName(clothesAttributeDefCreateRequest.name())) {
      throw new AttributeDefAlreadyExistsException();
    }

    AttributeDef attributeDef = new AttributeDef(
        clothesAttributeDefCreateRequest.name(),
        clothesAttributeDefCreateRequest.selectableValues()
    );

    return attributeDefMapper.toDto(attributeDefRepository.save(attributeDef));

  }


  /**
   * Retrieves a paginated list of clothing attribute definitions with optional sorting and keyword filtering.
   *
   * @param cursor        The pagination cursor indicating the starting point for the next page.
   * @param idAfter       The ID after which to start fetching results, used for pagination.
   * @param limit         The maximum number of records to return.
   * @param sortBy        The field by which to sort the results.
   * @param sortDirection The direction of sorting ("asc" or "desc").
   * @param keywordLike   A keyword for filtering attribute definitions by name.
   * @return A {@code PageResponse} containing the list of attribute definition DTOs, pagination cursors, flags, total count, and sorting information.
   */
  @Transactional(readOnly = true)
  public PageResponse<ClothesAttributeDefDto> findAll(String cursor, String idAfter, int limit,
      String sortBy,
      String sortDirection, String keywordLike) {

    //의상 목록 가져오기
    List<AttributeDef> attributeDefs = attributeDefRepository.findAllByCursor(cursor, idAfter,
        limit + 1,
        sortBy, sortDirection, keywordLike);

    //실제 size계산 (초과 조회된 1개 제외)
    int fetchedSize = attributeDefs.size();
    boolean hasNext = fetchedSize > limit;

    //실제 보여줄 limit 수만큼 clothes 남기기
    List<AttributeDef> result = hasNext ? attributeDefs.subList(0, limit) : attributeDefs;

    //DTO 변환
    List<ClothesAttributeDefDto> attributeDefDtos = result.stream()
        .map(attributeDefMapper::toDto)
        .toList();
    int size = attributeDefDtos.size();

    //TODO: 매번 호출 비효율 -> 캐싱?
    //totalCount
    //sortBy 는 name 디폴트?
    int totalCount = attributeDefRepository.getTotalCounts(sortBy, keywordLike);

    // next 조회
    // default name?
    String nextCursor = hasNext ? result.get(size - 1).getName() : null;
    String nextIdAfter = hasNext ? result.get(size - 1).getId().toString() : null;

    return new PageResponse<>(attributeDefDtos, nextCursor, nextIdAfter, hasNext, totalCount,
        sortBy, sortDirection);
  }


  /**
   * Updates an existing clothing attribute definition with new name and selectable values.
   *
   * Throws an exception if the new name already exists or if the attribute definition is not found.
   *
   * @param definitionId the unique identifier of the attribute definition to update
   * @param updateRequest the update request containing the new name and selectable values
   * @return the updated attribute definition as a DTO
   */
  @Transactional
  public ClothesAttributeDefDto update(UUID definitionId,
      ClothesAttributeDefUpdateRequest updateRequest) {

    //중복 의상 속성 네임 검증
    if (attributeDefRepository.existsByName(updateRequest.name())) {
      throw new AttributeDefAlreadyExistsException();
    }

    // attributeDef 검색
    AttributeDef attributeDef = attributeDefRepository.findById(definitionId)
        .orElseThrow(() -> new AttributeDefNotFoundException().withId(definitionId));

    attributeDef.update(updateRequest.name(), updateRequest.selectableValues());

    return attributeDefMapper.toDto(attributeDefRepository.save(attributeDef));
  }

  /**
   * Deletes an attribute definition by its ID and returns its DTO representation.
   *
   * @param attributeDefId the unique identifier of the attribute definition to delete
   * @return the DTO of the deleted attribute definition
   * @throws AttributeDefNotFoundException if no attribute definition with the given ID exists
   */
  @Transactional
  public ClothesAttributeDefDto delete(UUID attributeDefId) {

    AttributeDef attributeDef = attributeDefRepository.findById(attributeDefId)
        .orElseThrow(() -> new AttributeDefNotFoundException().withId(attributeDefId));

    ClothesAttributeDefDto clothesAttributeDefDto = attributeDefMapper.toDto(attributeDef);

    //의상 속성 삭제
    attributeDefRepository.deleteById(attributeDefId);

    //TODO: 삭제시 중간테이블 정보 삭제

    return clothesAttributeDefDto;
  }

}
