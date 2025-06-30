package com.codeit.sb01otbooteam06.domain.clothes.service;

import static com.codeit.sb01otbooteam06.domain.clothes.entity.QClothes.clothes;

import com.codeit.sb01otbooteam06.domain.clothes.entity.Clothes;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesCreateRequset;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesDto;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesUpdateRequest;
import com.codeit.sb01otbooteam06.domain.clothes.exception.ClothesNotFoundException;
import com.codeit.sb01otbooteam06.domain.clothes.mapper.ClothesMapper;
import com.codeit.sb01otbooteam06.domain.clothes.repository.ClothesRepository;
import com.codeit.sb01otbooteam06.domain.user.entity.User;
import com.codeit.sb01otbooteam06.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClothesService {

  private final ClothesRepository clothesRepository;
  private final UserRepository userRepository;

  private final AttributeService attributeService;
  private final ClothesAttributeService clothesAttributeService;

  private final ClothesMapper clothesMapper;

  //S3 이미지 저장 디렉토리 네임
  private final String directory = clothes.getClass().getSimpleName();

  /**
   * 의상을 등록합니다.
   *
   * @param clothesCreateRequset
   * @param clothesImage
   * @return ClothesDto
   */
  @Transactional
  public ClothesDto create(ClothesCreateRequset clothesCreateRequset, MultipartFile clothesImage) {

    //TODO: User 예외던지기
    User owner = userRepository.findById(clothesCreateRequset.ownerId()).orElseThrow();

    //TODO: S3 업로드 로직 필요
    String imageUrl = "";

    Clothes clothes = new Clothes(
        owner,
        clothesCreateRequset.name(),
        clothesCreateRequset.type(),
        imageUrl
    );

    //DB에 의상 엔티티 저장
    clothesRepository.save(clothes);

    //TODO: 이미지-속성 중간테이블 저장 로직
    clothesAttributeService.create(clothes.getId(), clothesCreateRequset.attributes());

    return clothesMapper.toDto(clothes);
  }

  /**
   * 의상을 수정합니다.
   *
   * @param clothesID
   * @param clothesUpdateRequest
   * @param clothesImage
   * @return ClothesDto
   */
  @Transactional
  public ClothesDto update(UUID clothesID, ClothesUpdateRequest clothesUpdateRequest,
      MultipartFile clothesImage) {

    //의상 존재 확인
    Clothes clothes = clothesRepository.findById(clothesID)
        .orElseThrow(() -> new ClothesNotFoundException().withId(clothesID));

    // todo: 이미지가 새로 들어온 경우에 S3에 업로드
    String imageUrl = clothes.getImageUrl();
//    if (clothesImage != null) {
//      imageUrl = s3Service.upload(clothesImage, directory);
//    }

    //의상 정보 업데이트
    clothes.update(
        clothesUpdateRequest.name(),
        clothesUpdateRequest.type(),
        imageUrl
    );

    //의상에 대한 의상속성중간테이블 업데이트
    clothesAttributeService.update(clothesID, clothesUpdateRequest.attributes());

    return clothesMapper.toDto(clothes);

  }

  //TODO: 의상 추천 알고리즘

  /**
   * 의상을 삭제합니다.
   *
   * @param clothesId
   */
  @Transactional
  public void delete(UUID clothesId) {
    clothesRepository.findById(clothesId)
        .orElseThrow(() -> new ClothesNotFoundException().withId(clothesId));
    clothesRepository.deleteById(clothesId);

  }

}
