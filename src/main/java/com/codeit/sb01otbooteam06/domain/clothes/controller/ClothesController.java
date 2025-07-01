package com.codeit.sb01otbooteam06.domain.clothes.controller;

import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesCreateRequset;
import com.codeit.sb01otbooteam06.domain.clothes.entity.dto.ClothesDto;
import com.codeit.sb01otbooteam06.domain.clothes.service.ClothesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clothes")
public class ClothesController {

  private final ClothesService clothesService;

  //TODO: VAlid 설정
  @PostMapping
  public ResponseEntity<ClothesDto> addClothes(
      @RequestPart(value = "request")
      ClothesCreateRequset clothesCreateRequset,
      @RequestPart(value = "image") MultipartFile file) {
    ClothesDto clothesDto = clothesService.create(clothesCreateRequset, file);
    return ResponseEntity.ok(clothesDto);
  }

}
