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
   * Saves a list of attributes for the specified Clothes entity by creating and persisting ClothesAttribute entities.
   *
   * For each provided attribute DTO, retrieves the corresponding AttributeDef by UUID, creates a ClothesAttribute linking the Clothes and AttributeDef with the given value, and saves it. Throws AttributeDefNotFoundException if any attribute definition is not found.
   *
   * @param clothes    the Clothes entity to associate attributes with
   * @param attributes the list of ClothesAttributeDto objects representing the attributes to save
   * @return a list of saved ClothesAttribute entities associated with the Clothes
   */
  @Transactional
  public List<ClothesAttribute> create(Clothes clothes, List<ClothesAttributeDto> attributes) {
    List<ClothesAttribute> savedAttributes = new ArrayList<>();

    //todo: value가 속성리스트에 포함되어있는지 예외체크

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

  /**
   * Updates the attributes associated with a given Clothes entity by replacing all existing attributes with the provided list.
   *
   * @param clothes the Clothes entity whose attributes are to be updated
   * @param attributes the new list of attribute DTOs to associate with the Clothes entity
   * @return a list of newly saved ClothesAttribute entities reflecting the updated attributes
   */
  @Transactional
  public List<ClothesAttribute> update(Clothes clothes, List<ClothesAttributeDto> attributes) {

    //의상에 대한 기존 중간테이블 삭제 후 생성
    clothesAttributeRepository.deleteByClothes(clothes);
    return this.create(clothes, attributes);

  }
}
