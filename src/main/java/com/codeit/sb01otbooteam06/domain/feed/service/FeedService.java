package com.codeit.sb01otbooteam06.domain.feed.service;

import com.codeit.sb01otbooteam06.domain.feed.dto.response.FeedDto;
import com.codeit.sb01otbooteam06.domain.feed.dto.request.FeedCreateRequest;
import com.codeit.sb01otbooteam06.domain.feed.dto.request.FeedUpdateRequest;
import com.codeit.sb01otbooteam06.domain.feed.dto.response.FeedDtoCursorResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface FeedService {

  FeedDto createFeed(FeedCreateRequest request);

  FeedDto getFeed(UUID feedId);


  FeedDto updateFeed(UUID feedId, FeedUpdateRequest request);

  void deleteFeed(UUID feedId);

  FeedDtoCursorResponse getFeedsByCursor(
      String keyword,
      String skyStatus,
      String precipitationType,
      Instant cursorCreatedAt,
      UUID cursorId,
      Long cursorLikeCount,
      int size,
      String sortBy
  );

}
