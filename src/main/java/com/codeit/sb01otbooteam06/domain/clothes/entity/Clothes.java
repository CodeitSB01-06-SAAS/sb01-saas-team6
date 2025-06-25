package com.codeit.sb01otbooteam06.domain.clothes.entity;

import com.codeit.sb01otbooteam06.domain.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "clothes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clothes extends BaseUpdatableEntity {

  //todo: user 연결하기
//  @Column(name = "owner_id", nullable = false)
//  private User user;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "type", nullable = false)
  private String type;

  public Clothes(String name, String type) {
    this.name = name;
    this.type = type;

  }
}
