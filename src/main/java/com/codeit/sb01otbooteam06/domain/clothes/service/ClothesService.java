package com.codeit.sb01otbooteam06.domain.clothes.service;

import static com.codeit.sb01otbooteam06.domain.clothes.entity.QClothes.clothes;

import com.codeit.sb01otbooteam06.domain.clothes.entity.Clothes;
import com.codeit.sb01otbooteam06.domain.clothes.entity.ClothesAttribute;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeWithDefDto;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesCreateRequset;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesDto;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesUpdateRequest;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.PageResponse;
import com.codeit.sb01otbooteam06.domain.clothes.exception.ClothesNotFoundException;
import com.codeit.sb01otbooteam06.domain.clothes.mapper.ClothesAttributeWithDefDtoMapper;
import com.codeit.sb01otbooteam06.domain.clothes.mapper.ClothesMapper;
import com.codeit.sb01otbooteam06.domain.clothes.repository.ClothesAttributeRepository;
import com.codeit.sb01otbooteam06.domain.clothes.repository.ClothesRepository;
import com.codeit.sb01otbooteam06.domain.user.entity.Role;
import com.codeit.sb01otbooteam06.domain.user.entity.User;
import com.codeit.sb01otbooteam06.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClothesService {

  private final ClothesRepository clothesRepository;
  private final UserRepository userRepository;

  private final AttributeDefService attributeDefService;
  private final ClothesAttributeService clothesAttributeService;

  private final ClothesMapper clothesMapper;
  private final ClothesAttributeWithDefDtoMapper clothesAttributeWithDefDtoMapper;

  //S3 이미지 저장 디렉토리 네임
  private final String directory = clothes.getClass().getSimpleName();
  private final ClothesAttributeRepository clothesAttributeRepository;


  @PersistenceContext
  private EntityManager entityManager;

  /**
   * 의상을 등록합니다.
   *
   * @param clothesCreateRequset
   * @param clothesImage
   * @return ClothesDto
   */
  @Transactional
  public ClothesDto create(ClothesCreateRequset clothesCreateRequset, MultipartFile clothesImage) {

//    //TODO: User 예외처리, 
//    User owner = userRepository.findById(clothesCreateRequset.ownerId()).orElseThrow();

    //TODO: 더미 유저 삭제하기
    //dummy
    User owner = User.builder()
        .email("dummy@example.com")
        .password("password123")  //
        .name("더미 사용자")
        .role(Role.USER)          //
        .locked(false)
        .linkedOAuthProviders(List.of("google", "kakao")) // 임의의 OAuth 제공자 리스트
        .build();
    userRepository.save(owner);

    //TODO: S3 업로드 로직 필요
    String imageUrl = "";
    //String imageUrl = s3Service.upload(clothesImage, directory);

    Clothes clothes = new Clothes(
        owner,
        clothesCreateRequset.name(),
        clothesCreateRequset.type(),
        imageUrl
    );

    //DB에 의상 엔티티 저장
    clothesRepository.save(clothes);

    List<ClothesAttribute> clothesAttributes = clothesAttributeService.create(clothes,
        clothesCreateRequset.attributes());

    ClothesDto clothesDto = clothesMapper.toDto(clothes);

    return new ClothesDto(
        clothesDto.id(),
        clothesDto.ownerId(),
        clothesDto.name(),
        clothesDto.imageUrl(),
        clothesDto.type(),
        makeClothesAttributeWithDefDtos(clothesAttributes) // attribute
    );
  }


  /**
   * ClothesDto의 attributes (List<ClothesAttributeWithDefDto> dto 를 생성합니다.
   *
   * @param attributes
   * @returnList<ClothesAttributeWithDefDto>
   */
  private List<ClothesAttributeWithDefDto> makeClothesAttributeWithDefDtos(
      List<ClothesAttribute> attributes) {
    return attributes.stream()
        .map(clothesAttributeWithDefDtoMapper::toDto)
        .toList();
  }

  /**
   * ClothesDto를 만듭니다
   *
   * @param clothes
   * @param clothesAttributes
   * @return ClothesDto
   */
  private ClothesDto makeClothesDto(Clothes clothes, List<ClothesAttribute> clothesAttributes) {

    ClothesDto clothesDto = clothesMapper.toDto(clothes);
    return new ClothesDto(
        clothesDto.id(),
        clothesDto.ownerId(),
        clothesDto.name(),
        clothesDto.imageUrl(),
        clothesDto.type(),
        makeClothesAttributeWithDefDtos(clothesAttributes) // attribute
    );

  }


  /**
   * 커서 기반 페이지네이션으로 의상 목록을 조회합니다.
   *
   * @param cursor
   * @param idAfter
   * @param limit
   * @param typeEqual
   * @param ownerId
   * @return PageResponse<ClothesDto>
   */
  @Transactional(readOnly = true)
  public PageResponse<ClothesDto> findAll(String cursor, String idAfter,
      int limit, String typeEqual, UUID ownerId) {

    //의상 목록 가져오기
    List<Clothes> clothesList = clothesRepository.findAllByCursor(cursor, idAfter, limit + 1,
        typeEqual, ownerId);

    //실제 size계산 (초과 조회된 1개 제외)
    int fetchedSize = clothesList.size();
    boolean hasNext = fetchedSize > limit;

    //실제 보여줄 limit 수만큼 clothes 남기기
    List<Clothes> resultClothes = hasNext ? clothesList.subList(0, limit) : clothesList;

    ///  dto 변환 로직
    //결과를 담을 clothesDto리스트 
    List<ClothesDto> clothesDtos = new ArrayList<>();

    // 의상 결과 리스트에 대하여 dto 변환 수행
    for (Clothes clothes : resultClothes) {
      //의상에 대한 속성
      List<ClothesAttribute> clothesAttributes = clothesAttributeRepository.findByClothes(clothes);

      ClothesDto clothesDto = clothesMapper.toDto(clothes);

      clothesDtos.add(new ClothesDto(
          clothesDto.id(),
          clothesDto.ownerId(),
          clothesDto.name(),
          clothesDto.imageUrl(),
          clothesDto.type(),
          makeClothesAttributeWithDefDtos(clothesAttributes) // attributes
      ));

    }
    ///

    int size = resultClothes.size();

    //TODO: 매번 호출 비효율 -> 캐싱?
    //totalCount
    int totalCount = clothesRepository.getTotalCounts(typeEqual, ownerId);

    // next 조회
    String nextCursor = hasNext ? resultClothes.get(size - 1).getCreatedAt().toString() : null;
    String nextIdAfter = hasNext ? resultClothes.get(size - 1).getId().toString() : null;

    return new PageResponse<>(clothesDtos, nextCursor, nextIdAfter, hasNext, totalCount,
        "createdAt", "DESCENDING");

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

    // 의상에 대한 의상속성중간테이블 업데이트
    List<ClothesAttribute> clothesAttributes =
        clothesAttributeService.update(clothes, clothesUpdateRequest.attributes());

    ClothesDto clothesDto = clothesMapper.toDto(clothes);

    return new ClothesDto(
        clothesDto.id(),
        clothesDto.ownerId(),
        clothesDto.name(),
        clothesDto.imageUrl(),
        clothesDto.type(),
        makeClothesAttributeWithDefDtos(clothesAttributes) // attributes
    );


  }

  //TODO: 의상 추천 알고리즘
  //날씨 데이터, 사용자가 등록한 의상, 프로필 정보를 활용하여 의상을 추천

  /**
   * 의상을 삭제합니다.
   *
   * @param clothesId
   */
  @Transactional
  public void delete(UUID clothesId) {
    //todo: 의상 삭제시 중간테이블 삭제

    clothesRepository.findById(clothesId)
        .orElseThrow(() -> new ClothesNotFoundException().withId(clothesId));
    clothesRepository.deleteById(clothesId);

  }

}
