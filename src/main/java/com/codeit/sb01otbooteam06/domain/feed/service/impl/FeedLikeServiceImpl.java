package com.codeit.sb01otbooteam06.domain.feed.service.impl;

import com.codeit.sb01otbooteam06.domain.feed.entity.Feed;
import com.codeit.sb01otbooteam06.domain.feed.entity.FeedLike;
import com.codeit.sb01otbooteam06.domain.feed.repository.FeedLikeRepository;
import com.codeit.sb01otbooteam06.domain.feed.repository.FeedRepository;
import com.codeit.sb01otbooteam06.domain.feed.service.FeedLikeService;
import com.codeit.sb01otbooteam06.domain.user.repository.UserRepository;
import com.codeit.sb01otbooteam06.global.exception.ErrorCode;
import com.codeit.sb01otbooteam06.global.exception.OtbooException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedLikeServiceImpl implements FeedLikeService {

  private final FeedLikeRepository feedLikeRepository;
  private final UserRepository userRepository;
  private final FeedRepository feedRepository;
  //private final AuthService authService;


  @Override
  @Transactional
  public void likeFeed(UUID feedId) {
    /**
      todo : 좋아요 누르는 유저 아이디를 받아오는 서비스 사용해서 받아 와야 함.
      UUID userId = authService.getCurrentUserId();
      User user = userRepository.findById(userId)
        .orElseThrow(() -> new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR));
     */

    Feed feed = feedRepository.findById(feedId)
        .orElseThrow(() -> new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR));

    // 좋아요 중복 로직
    // if(feedLikeRepository.existsByFeedAndUser(feed, user)){
    //  throw new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR)
    // }

    // 좋아요 생성 & DB 저장
    // FeedLike like = new FeedLike(feed, user)
    // feedRepository.save(like)

    //피드 카운드 증가 -> dirty checking 으로 자동 반영
    feed.like();

  }

  @Transactional
  @Override
  public void unlikeFeed(UUID feedId) {
    // UUID userId = authService.getCurrentUserId();
    // User user = userRepository.findById(userId)
    //    .orElseThrow(() -> new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR));

    Feed feed = feedRepository.findById(feedId)
        .orElseThrow(() -> new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR));

    // 기존 좋아요 엔티티 조회
    //  FeedLike like = feedLikeRepository.findByFeedAndUser(feed, user)
    //    .orElseThrow(() -> new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR));

    // 좋아요 취소
    // feedLikeRepository.delete(like);

    // 피드 좋아요 수 감소
    feed.unlike();

  }
}
