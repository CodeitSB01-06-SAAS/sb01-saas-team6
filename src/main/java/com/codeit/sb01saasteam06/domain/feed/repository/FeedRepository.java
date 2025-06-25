package com.codeit.sb01saasteam06.domain.feed.repository;

import com.codeit.sb01saasteam06.domain.feed.entity.Feed;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, UUID> {


  @Query("""
      SELECT f FROM Feed f
      JOIN FETCH f.weather w
      WHERE (:keyword IS NULL 
             OR LOWER(f.content) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:skyStatus IS NULL OR w.skyStatus = :skyStatus)
        AND (:precipType IS NULL 
             OR w.precipitationType = :precipitationType)
        AND ( :cursorCreatedAt IS NULL
              OR (f.createdAt < :cursorCreatedAt
                  OR (f.createdAt = :cursorCreatedAt 
                      AND f.id < :cursorId)) )
      ORDER BY f.createdAt DESC, f.id DESC
      """)
  List<Feed> findFeedsByCreatedAtCursor(
      @Param("keyword") String keyword,
      @Param("skyStatus") String skyStatus,
      @Param("precipitationType") String precipitationType,
      @Param("cursorCreatedAt") Instant cursorCreatedAt,
      @Param("cursorId") UUID cursorId,
      Pageable pageable
  );

  @Query("""
      SELECT f FROM Feed f
      JOIN FETCH f.weather w
      WHERE (:keyword IS NULL 
             OR LOWER(f.content) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:skyStatus IS NULL OR w.skyStatus = :skyStatus)
        AND (:precipitationType IS NULL OR w.precipitationType = :precipitationType)
        AND (
          :cursorLikeCount IS NULL
          OR (f.likeCount < :cursorLikeCount
              OR (f.likeCount = :cursorLikeCount 
                  AND f.id < :cursorId))
        )
      ORDER BY f.likeCount DESC, f.id DESC
      """)
  List<Feed> findFeedsByLikeCountCursor(
      @Param("keyword") String keyword,
      @Param("skyStatus") String skyStatus,
      @Param("precipitationType") String precipitationType,
      @Param("cursorLikeCount") Long cursorLikeCount,
      @Param("cursorId") UUID cursorId,
      Pageable pageable
  );


}
