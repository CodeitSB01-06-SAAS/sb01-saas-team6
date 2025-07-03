package com.codeit.sb01otbooteam06.domain.feed.service;

import java.util.UUID;

public interface FeedLikeService {

  void likedFeed(UUID feedId);

  void unlikedFeed(UUID feedId);

}
