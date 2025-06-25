package com.codeit.sb01otbooteam06.domain.follow.repository;


import com.codeit.sb01otbooteam06.domain.follow.entity.Follow;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, UUID> {

  boolean existsByFollowerIdAndFolloweeId(UUID followerId, UUID followeeId);

  @Modifying
  void deleteByFollowerIdAndFolloweeId(UUID followerId, UUID followeeId);

  long countByFollowerId(UUID followerId);   // 내가 팔로잉하는 수

  long countByFolloweeId(UUID followeeId);   // 나를 팔로우하는 수

  @Query("""
          SELECT f
            FROM Follow f
           WHERE f.followeeId = :followee
             AND ( :cursor IS NULL OR f.id < :cursor )
        ORDER BY f.id DESC
      """)
  List<Follow> findFollowers(UUID followee,
      UUID cursor,
      Pageable page);

  @Query("""
          SELECT f
            FROM Follow f
           WHERE f.followerId = :follower
             AND ( :cursor IS NULL OR f.id < :cursor )
        ORDER BY f.id DESC
      """)
  List<Follow> findFollowings(UUID follower,
      UUID cursor,
      Pageable page);
}
