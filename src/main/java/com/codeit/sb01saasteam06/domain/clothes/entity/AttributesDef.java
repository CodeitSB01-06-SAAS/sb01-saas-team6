package com.codeit.sb01saasteam06.domain.clothes.entity;

import com.codeit.sb01saasteam06.domain.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@Table(name = "attributes_defs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttributesDef extends BaseUpdatableEntity {

  //의상속성
  @Column(name = "name", nullable = false)
  private String name;

  @JdbcTypeCode(SqlTypes.ARRAY)
  @Column(name = "selectable_values", columnDefinition = "text[]", nullable = false)
  private String[] selectableValues;

  public AttributesDef(String name, String[] selectableValues) {
    this.name = name;
    this.selectableValues = selectableValues;
  }

}
