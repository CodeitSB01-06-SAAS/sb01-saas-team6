package com.codeit.sb01saasteam06.domain.clothes.entity;

import com.codeit.sb01saasteam06.domain.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "clothes_attributes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClothesAttribute extends BaseUpdatableEntity {

  //의상-의상속성 중간 테이블

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "clothes_id", nullable = false)
  private Clothes clothes;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "attribute_id", nullable = false)
  private AttributesDef attributesDef;

  @Column(name = "value", nullable = false)
  private String value;

  public ClothesAttribute(Clothes clothes, AttributesDef attributesDef, String value) {
    this.clothes = clothes;
    this.attributesDef = attributesDef;
    this.value = value;
  }
}
