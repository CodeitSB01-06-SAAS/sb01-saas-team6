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
import com.codeit.sb01otbooteam06.domain.user.entity.User;
import com.codeit.sb01otbooteam06.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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

  /****
   * Registers a new clothing item and returns its detailed DTO.
   *
   * Creates a new `Clothes` entity with the provided name, type, and image, associating it with the admin user. Saves the entity, creates its attributes, and returns a `ClothesDto` including all associated attributes.
   *
   * @param clothesCreateRequset the request containing clothing details and attributes
   * @param clothesImage the image file for the clothing item
   * @return a `ClothesDto` representing the newly created clothing item with its attributes
   */
  @Transactional
  public ClothesDto create(ClothesCreateRequset clothesCreateRequset, MultipartFile clothesImage) {

//    //TODO: User 찾기, 예외처리,
//    User owner = userRepository.findById(clothesCreateRequset.ownerId()).orElseThrow();

    /// TODO: 임시: 현재 의상을 ownerId로 찾지않고 admin 유저에 등록 중
    User owner = userRepository.findByEmail("admin@example.com")
        .orElseThrow(() -> new NoSuchElementException());

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

    return makeClothesDto(clothes, clothesAttributes);
  }


  /**
   * Converts a list of ClothesAttribute entities to a list of ClothesAttributeWithDefDto DTOs.
   *
   * @param attributes the list of ClothesAttribute entities to convert
   * @return a list of ClothesAttributeWithDefDto representing the provided attributes
   */
  private List<ClothesAttributeWithDefDto> makeClothesAttributeWithDefDtos(
      List<ClothesAttribute> attributes) {
    return attributes.stream()
        .map(clothesAttributeWithDefDtoMapper::toDto)
        .toList();
  }

  /**
   * Constructs a ClothesDto from a Clothes entity and its associated attributes.
   *
   * @param clothes the Clothes entity to convert
   * @param clothesAttributes the list of ClothesAttribute entities associated with the clothes
   * @return a ClothesDto containing the clothes data and its mapped attributes
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
   * Retrieves a paginated list of clothes using cursor-based pagination, with optional filtering by type and owner.
   *
   * @param cursor      The pagination cursor representing the starting point for the next page.
   * @param idAfter     The ID to disambiguate items with the same cursor value.
   * @param limit       The maximum number of clothes to return.
   * @param typeEqual   Optional filter to return only clothes of a specific type.
   * @param ownerId     Optional filter to return only clothes owned by the specified user.
   * @return A PageResponse containing a list of ClothesDto, pagination metadata, and total count.
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
      //결과리스트에 clothesdto추가
      clothesDtos.add(makeClothesDto(clothes, clothesAttributes)
      );
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
   * Updates an existing clothing item with new details and attributes.
   *
   * If the name or type fields in the update request are null, the existing values are retained. The image is updated only if a new image is provided; otherwise, the current image URL is kept. Associated clothing attributes are also updated.
   *
   * @param clothesID the unique identifier of the clothing item to update
   * @param clothesUpdateRequest the request containing updated fields and attributes
   * @param clothesImage the new image file for the clothing item, or null to retain the current image
   * @return the updated clothing item as a ClothesDto, including its attributes
   * @throws ClothesNotFoundException if the clothing item with the specified ID does not exist
   */
  @Transactional
  public ClothesDto update(UUID clothesID, ClothesUpdateRequest clothesUpdateRequest,
      MultipartFile clothesImage) {

    //의상 존재 확인
    Clothes clothes = clothesRepository.findById(clothesID)
        .orElseThrow(() -> new ClothesNotFoundException().withId(clothesID));

    // name, type의 수저이 없는 경우 예외처리
    String newName = clothesUpdateRequest.name();
    String newType = clothesUpdateRequest.type();
    if (clothesUpdateRequest.name() == null) {
      newName = clothes.getName();
    }
    if (clothesUpdateRequest.type() == null) {
      newType = clothes.getType();
    }

    // todo: 이미지가 새로 들어온 경우에 S3에 업로드
    String imageUrl = clothes.getImageUrl();
//    if (clothesImage != null) {
//      imageUrl = s3Service.upload(clothesImage, directory);
//    }

    //의상 정보 업데이트
    clothes.update(
        newName,
        newType,
        imageUrl
    );

    // 의상에 대한 의상속성중간테이블 업데이트
    List<ClothesAttribute> clothesAttributes =
        clothesAttributeService.update(clothes, clothesUpdateRequest.attributes());

    return makeClothesDto(clothes, clothesAttributes);


  }

  //TODO: (심화) 구매 링크로 의상정보 불러오기?

  /**
   * Deletes a clothing item by its unique identifier.
   *
   * Throws a {@code ClothesNotFoundException} if the specified clothing item does not exist.
   *
   * @param clothesId the unique identifier of the clothing item to delete
   */
  @Transactional
  public void delete(UUID clothesId) {
    //todo: 의상 삭제시 중간테이블 삭제 ->

    clothesRepository.findById(clothesId)
        .orElseThrow(() -> new ClothesNotFoundException().withId(clothesId));
    clothesRepository.deleteById(clothesId);

  }

}
