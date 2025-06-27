package com.codeit.sb01otbooteam06.domain.feed.service.impl;

import com.codeit.sb01otbooteam06.domain.feed.dto.request.CommentCreateRequest;
import com.codeit.sb01otbooteam06.domain.feed.dto.response.CommentDto;
import com.codeit.sb01otbooteam06.domain.feed.dto.response.CommentDtoCursorResponse;
import com.codeit.sb01otbooteam06.domain.feed.dto.response.FeedDto;
import com.codeit.sb01otbooteam06.domain.feed.entity.Comment;
import com.codeit.sb01otbooteam06.domain.feed.entity.Feed;
import com.codeit.sb01otbooteam06.domain.feed.repository.CommentRepository;
import com.codeit.sb01otbooteam06.domain.feed.repository.FeedRepository;
import com.codeit.sb01otbooteam06.domain.feed.service.CommentService;
import com.codeit.sb01otbooteam06.domain.user.entity.User;
import com.codeit.sb01otbooteam06.domain.user.repository.UserRepository;
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
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final FeedRepository feedRepository;
  private final UserRepository userRepository;

  @Transactional
  @Override
  public FeedDto createComment(CommentCreateRequest request) {
    //todo : 나중에 인증 관련에서 개발 할때 리팩토링.
    User author = userRepository.findById(request.getAuthorId())
        .orElseThrow(() -> new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR));
    Feed feed = feedRepository.findById(request.getFeedId())
        .orElseThrow(() -> new OtbooException(ErrorCode.ILLEGAL_ARGUMENT_ERROR));

    Comment comment = Comment.of(request.getContent(), author.getName(), author, feed);

    // todo : 피드의 comment 리스트에 댓글 엔티티 추가.? 나중에 확인

    // 댓글을 레포지토리에 저장안한 이유? cascade = ALL 설정, 피드가 저장 되면 같이 저장.
    feed.addComment(comment);
    feedRepository.save(feed);

    return FeedDto.fromEntity(feed);
  }

  @Override
  public CommentDtoCursorResponse getCommentsByCursor(UUID feedId, Instant cursor, UUID idAfter,
      int limit) {
    PageRequest pageReq = PageRequest.of(0, limit);

    List<Comment> comments = commentRepository.findCommentsByCreatedAtCursor(feedId, cursor,
        idAfter, pageReq);

    List<CommentDto> data = comments.stream().map(CommentDto::fromEntity).toList();

    boolean hasNext = data.size() == limit;
    String nextCursor = null;
    UUID nextIdAfter = null;

    if (hasNext) {
      Comment last = comments.get(comments.size() - 1);
      nextCursor = last.getCreatedAt().toString();
      nextIdAfter = last.getId();
    }

    long totalCount = commentRepository.countByFeedId(feedId);

    return new CommentDtoCursorResponse(data, nextCursor, nextIdAfter, hasNext, totalCount,
        "createdAt", "DESCENDING");
  }
}
