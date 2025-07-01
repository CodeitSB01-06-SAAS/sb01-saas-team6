package com.codeit.sb01otbooteam06.domain.clothes.service;

import com.codeit.sb01otbooteam06.domain.clothes.entity.AttributeDef;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDefCreateRequest;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDefDto;
import com.codeit.sb01otbooteam06.domain.clothes.exception.AttributeDefAlreadyExistsException;
import com.codeit.sb01otbooteam06.domain.clothes.exception.AttributeDefNotFoundException;
import com.codeit.sb01otbooteam06.domain.clothes.mapper.AttributeDefMapper;
import com.codeit.sb01otbooteam06.domain.clothes.repository.AttributeDefRepository;
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

  private final AttributeDefRepository attributeDefRepository;

  private final AttributeDefMapper attributeDefMapper;


  @Transactional
  public ClothesAttributeDefDto create(
      ClothesAttributeDefCreateRequest clothesAttributeDefCreateRequest) {

    //todo:  제거? 중복 의상 속성 검증
    if (attributeDefRepository.existsByName(clothesAttributeDefCreateRequest.name())) {
      throw new AttributeDefAlreadyExistsException();
    }

    AttributeDef attributeDef = new AttributeDef(
        clothesAttributeDefCreateRequest.name(),
        clothesAttributeDefCreateRequest.selectableValues().toArray(new String[0])
    );

    return attributeDefMapper.toDto(attributeDefRepository.save(attributeDef));

  }

  //TODO: 수정

  //TODO: 조회

  @Transactional
  public ClothesAttributeDefDto delete(UUID attributeDefId) {

    AttributeDef attributeDef = attributeDefRepository.findById(attributeDefId)
        .orElseThrow(() -> new AttributeDefNotFoundException().withId(attributeDefId));

    ClothesAttributeDefDto clothesAttributeDefDto = attributeDefMapper.toDto(attributeDef);

    attributeDefRepository.deleteById(attributeDefId);

    return clothesAttributeDefDto;
  }

}
