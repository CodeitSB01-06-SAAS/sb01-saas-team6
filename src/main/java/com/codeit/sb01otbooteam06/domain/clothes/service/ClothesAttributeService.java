package com.codeit.sb01otbooteam06.domain.clothes.service;


import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesAttributeDto;
import com.codeit.sb01otbooteam06.domain.clothes.repository.ClothesAttributeRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClothesAttributeService {

  /// 의상-속성 중간 테이블 서비스

  private final ClothesAttributeRepository clothesAttributeRepository;

  public void create(UUID id, List<ClothesAttributeDto> attributes) {

  }

  public void update(UUID clothesID, List<ClothesAttributeDto> attributes) {
  }
}
