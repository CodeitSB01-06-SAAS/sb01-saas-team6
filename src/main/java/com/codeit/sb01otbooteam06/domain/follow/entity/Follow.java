package com.codeit.sb01otbooteam06.domain.follow.entity;


import com.codeit.sb01otbooteam06.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "follows",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_follows_pair",
        columnNames = {"follower_id", "followee_id"}
    ))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseEntity {

  //아직 User 엔티티 없으므로 FK 대신 UUID
  @Column(name = "follower_id", nullable = false, columnDefinition = "UUID")
  private UUID followerId;

  @Column(name = "followee_id", nullable = false, columnDefinition = "UUID")
  private UUID followeeId;


  private Follow(UUID followerId, UUID followeeId) {
    this.followerId = followerId;
    this.followeeId = followeeId;
  }

  public static Follow from(UUID followerId, UUID followeeId) {
    return new Follow(followerId, followeeId);
  }
}

