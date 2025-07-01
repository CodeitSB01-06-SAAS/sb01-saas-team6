package com.codeit.sb01otbooteam06.domain.clothes.service;

import com.codeit.sb01otbooteam06.domain.clothes.entity.AttributeDef;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDefCreateRequest;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDto;
import com.codeit.sb01otbooteam06.domain.clothes.repository.AttributeDefRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttributeDefService {

  /// 의상 속성 서비스
  private final AttributeDefRepository attributeDefRepository;

  public ClothesAttributeDto create(
      ClothesAttributeDefCreateRequest clothesAttributeDefCreateRequest) {

    if (attributeDefRepository.existsByName(clothesAttributeDefCreateRequest.name())) {
      new CLOTHES

    }

    AttributeDef attributeDef = new AttributeDef(
        clothesAttributeDefCreateRequest.name(),
        clothesAttributeDefCreateRequest.selectableValues().toArray(new String[0])
    );


  }

}
