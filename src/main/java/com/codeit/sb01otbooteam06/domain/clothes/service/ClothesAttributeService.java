package com.codeit.sb01otbooteam06.domain.clothes.service;


import com.codeit.sb01otbooteam06.domain.clothes.entity.AttributeDef;
import com.codeit.sb01otbooteam06.domain.clothes.entity.Clothes;
import com.codeit.sb01otbooteam06.domain.clothes.entity.ClothesAttribute;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDto;
import com.codeit.sb01otbooteam06.domain.clothes.exception.AttributeDefNotFoundException;
import com.codeit.sb01otbooteam06.domain.clothes.repository.AttributeDefRepository;
import com.codeit.sb01otbooteam06.domain.clothes.repository.ClothesAttributeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClothesAttributeService {

  /// 의상-속성 중간 테이블 서비스

  private final ClothesAttributeRepository clothesAttributeRepository;
  private final AttributeDefRepository attributeDefRepository;

  /**
   * 의상-속성 중간 테이블에 의상에 대한 속성을 저장합니다.
   *
   * @param clothes
   * @param attributes
   * @return 의상에 대한 ClothesAttribute 객체 리스트
   */
  @Transactional
  public List<ClothesAttribute> create(Clothes clothes, List<ClothesAttributeDto> attributes) {
    List<ClothesAttribute> savedAttributes = new ArrayList<>();

    //attributes는 리스트
    for (ClothesAttributeDto attribute : attributes) {
      // attributeDef 찾기
      AttributeDef targetAttributeDef = attributeDefRepository.findById(
              UUID.fromString(attribute.definitionId()))
          .orElseThrow(() -> new AttributeDefNotFoundException());

      ClothesAttribute clothesAttribute = new ClothesAttribute(
          clothes,
          targetAttributeDef,
          attribute.value()
      );

      //save
      savedAttributes.add(clothesAttributeRepository.save(clothesAttribute));
    }

    return savedAttributes;


  }

  public void update(UUID clothesID, List<ClothesAttributeDto> attributes) {
  }
}
