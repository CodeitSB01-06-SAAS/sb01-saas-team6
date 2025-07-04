package com.codeit.sb01otbooteam06.domain.feed.service.impl;

import com.codeit.sb01otbooteam06.domain.clothes.mapper.ClothesMapper;
import com.codeit.sb01otbooteam06.domain.feed.dto.response.FeedDto;
import com.codeit.sb01otbooteam06.domain.feed.dto.request.FeedCreateRequest;
import com.codeit.sb01otbooteam06.domain.feed.dto.request.FeedUpdateRequest;
import com.codeit.sb01otbooteam06.domain.feed.dto.response.FeedDtoCursorResponse;
import com.codeit.sb01otbooteam06.domain.feed.entity.Feed;
import com.codeit.sb01otbooteam06.domain.feed.repository.FeedRepository;
import com.codeit.sb01otbooteam06.domain.feed.service.FeedService;
import com.codeit.sb01otbooteam06.domain.user.entity.User;
import com.codeit.sb01otbooteam06.domain.user.repository.UserRepository;
import com.codeit.sb01otbooteam06.domain.weather.entity.Weather;
import com.codeit.sb01otbooteam06.domain.weather.mapper.WeatherDtoMapper;
import com.codeit.sb01otbooteam06.domain.weather.repository.WeatherRepository;
import com.codeit.sb01otbooteam06.global.exception.ErrorCode;
import com.codeit.sb01otbooteam06.global.exception.OtbooException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

  private final FeedRepository feedRepository;
  private final UserRepository userRepository;
  private final WeatherRepository weatherRepository;
  private final ClothesMapper clothesMapper;
  private final WeatherDtoMapper weatherDtoMapper;
  // 여기에 사용자 인증하는 서비스 추가.

  @Override
  @Transactional
  public FeedDto createFeed(FeedCreateRequest request) {

    // todo : 사용자 인증하는 메서드 추가
    User author = userRepository.findById(request.getAuthorId())
        .orElseThrow(() -> new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR));

    Weather weather = weatherRepository.findById(request.getWeatherId())
        .orElseThrow(() -> new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR));

    Feed feed = Feed.of(request.getContent(), author, weather);
    feedRepository.save(feed);
    return FeedDto.fromEntity(feed, clothesMapper,weatherDtoMapper);
  }

  @Transactional(readOnly = true)
  @Override
  public FeedDto getFeed(UUID feedId) {
    Feed feed = feedRepository.findById(feedId)
        .orElseThrow(() -> new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR));

    return FeedDto.fromEntity(feed, clothesMapper, weatherDtoMapper);
  }

  @Transactional
  @Override
  public FeedDto updateFeed(UUID feedId, FeedUpdateRequest request) {
    Feed feed = feedRepository.findById(feedId)
        .orElseThrow(() -> new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR));
    feed.updateContent(request.getContent());
    Feed updatedFeed = feedRepository.save(feed);

    return FeedDto.fromEntity(updatedFeed, clothesMapper, weatherDtoMapper);
  }

  @Transactional
  @Override
  public void deleteFeed(UUID feedId) {
    if(!feedRepository.existsById(feedId)){
      throw new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR);
    }
    feedRepository.deleteById(feedId);
  }


  @Transactional(readOnly = true)
  @Override
  public FeedDtoCursorResponse getFeedsByCursor(String keyword, String skyStatus, String precipitationType,
      Instant cursorCreatedAt, UUID cursorId, Long cursorLikeCount, int size, String sortBy) {

    PageRequest pageReq = PageRequest.of(0, size);
    List<Feed> feeds = "likeCount".equalsIgnoreCase(sortBy)
        ? feedRepository.findFeedsByLikeCountCursor(
        keyword, skyStatus, precipitationType, cursorLikeCount, cursorId, pageReq
    )
        : feedRepository.findFeedsByCreatedAtCursor(
            keyword, skyStatus, precipitationType, cursorCreatedAt, cursorId, pageReq
        );

    List<FeedDto> data = feeds.stream()
        .map(feed -> FeedDto.fromEntity(feed, clothesMapper,weatherDtoMapper))
        .toList();

    boolean hasNext = data.size() == size;
    String nextCursor = null;
    UUID nextIdAfter = null;

    if(hasNext) {
      Feed last = feeds.get(feeds.size() - 1);
      nextIdAfter = last.getId();
      nextCursor = "likeCount".equalsIgnoreCase(sortBy)
          ? String.valueOf(last.getLikeCount())
          : last.getCreatedAt().toString();
    }

    long totalCount = feedRepository.countByFilters(keyword, skyStatus, precipitationType);

    return new FeedDtoCursorResponse(
        data,
        nextCursor,
        nextIdAfter,
        hasNext,
        totalCount,
        sortBy,
        "DESC"
    );
  }
}
