package com.codeit.sb01otbooteam06.domain.feed.dto;

import com.codeit.sb01otbooteam06.domain.feed.entity.Feed;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedDto {

  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;

  // todo : 구현 한걸 확인 해야함.
  private AuthorDto author;
  private WeatherDto weather;
  private List<OotdDto> ootds;

  private String content;
  private long likeCount;
  private int commentsCount;
  private boolean likedByMe;

  public FeedDto(UUID id, Instant createdAt, Instant updatedAt, AuthorDto author,
      WeatherDto weather,
      List<OotdDto> ootds, String content, long likeCount, int commentsCount, boolean likedByMe) {
    this.id = id;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.author = author;
    this.weather = weather;
    this.ootds = ootds;
    this.content = content;
    this.likeCount = likeCount;
    this.commentsCount = commentsCount;
    this.likedByMe = likedByMe;
  }

  public static FeedDto fromEntity(Feed feed) {
    // todo: AuthorDto.fromEntity, WeatherDto.fromEntity, OotdDto.fromEntity 구현 필요
    AuthorDto authorDto = AuthorDto.fromEntity(feed.getUser());
    WeatherDto weatherDto = WeatherDto.fromEntity(feed.getWeather());
    List<OotdDto> ootdDtos = feed.getOotds().stream()
        .map(OotdDto::fromEntity)
        .toList();

    return new FeedDto(
        feed.getId(),
        feed.getCreatedAt(),
        feed.getUpdatedAt(),
        authorDto,
        weatherDto,
        ootdDtos,
        feed.getContent(),
        feed.getLikeCount(),
        feed.getCommentCount(),
        feed.isLikedByMe()
    );
  }

}
