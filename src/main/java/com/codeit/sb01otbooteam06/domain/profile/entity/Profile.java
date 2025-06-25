package com.codeit.sb01otbooteam06.domain.profile.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "profiles")
public class Profile {

  @Id
  @Column(nullable = false)
  private String id;  // users.id 참조 (1:1 관계)

  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Gender gender;

  @Column(name = "birth_date", nullable = false)
  private LocalDate birthDate;

  @Column(nullable = false)
  private double latitude;

  @Column(nullable = false)
  private double longitude;

  @Column(nullable = false)
  private int x;

  @Column(nullable = false)
  private int y;

  @ElementCollection
  @CollectionTable(name = "profile_location_names", joinColumns = @JoinColumn(name = "profile_id"))
  @Column(name = "location_name")
  private List<String> locationNames;

  @Column(name = "temperature_sensitivity", nullable = false)
  private int temperatureSensitivity;

  @Column(name = "profile_image_url")
  private String profileImageUrl;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
