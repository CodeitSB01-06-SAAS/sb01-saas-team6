package com.codeit.sb01otbooteam06.domain.feed.controller;

import com.codeit.sb01otbooteam06.domain.feed.dto.request.CommentCreateRequest;
import com.codeit.sb01otbooteam06.domain.feed.dto.request.FeedCreateRequest;
import com.codeit.sb01otbooteam06.domain.feed.dto.request.FeedUpdateRequest;
import com.codeit.sb01otbooteam06.domain.feed.dto.response.CommentDto;
import com.codeit.sb01otbooteam06.domain.feed.dto.response.CommentDtoCursorResponse;
import com.codeit.sb01otbooteam06.domain.feed.dto.response.FeedDto;
import com.codeit.sb01otbooteam06.domain.feed.dto.response.FeedDtoCursorResponse;
import com.codeit.sb01otbooteam06.domain.feed.service.CommentService;
import com.codeit.sb01otbooteam06.domain.feed.service.FeedLikeService;
import com.codeit.sb01otbooteam06.domain.feed.service.FeedService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {

  private final FeedService feedService;
  private final FeedLikeService likeService;
  private final CommentService commentService;

  @PostMapping
  public ResponseEntity<FeedDto> createFeed(@RequestBody @Valid FeedCreateRequest request) {
    FeedDto feed = feedService.createFeed(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(feed);
  }

  @GetMapping
  public ResponseEntity<FeedDtoCursorResponse> listFeedsByCursor(
      @RequestParam(name = "cursor", required = false) Instant cursor,
      @RequestParam(name = "idAfter", required = false) UUID idAfter,
      @RequestParam(name = "limit") int limit, @RequestParam(name = "sortBy") String sortBy,
      @RequestParam(name = "sortDirection") String sortDirection,
      @RequestParam(name = "keywordLike", required = false) String keywordLike,
      @RequestParam(name = "skyStatusEqual", required = false) String skyStatusEqual,
      @RequestParam(name = "precipitationTypeEqual", required = false) String precipitationTypeEqual,
      @RequestParam(name = "authorIdEqual", required = false) UUID authorIdEqual) {

    FeedDtoCursorResponse feedsByCursor = feedService.getFeedsByCursor(keywordLike, skyStatusEqual,
        precipitationTypeEqual, cursor, idAfter, null, limit, sortBy);

    return ResponseEntity.status(HttpStatus.OK).body(feedsByCursor);
  }

  @PatchMapping("/{feedId}")
  public ResponseEntity<FeedDto> updateFeed(@PathVariable UUID feedId,
      @RequestBody @Valid FeedUpdateRequest request) {

    FeedDto feed = feedService.updateFeed(feedId, request);

    return ResponseEntity.status(HttpStatus.OK).body(feed);
  }

  @DeleteMapping("/{feedId}")
  public ResponseEntity<FeedDto> deleteFeed(@PathVariable UUID feedId) {
    feedService.deleteFeed(feedId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping("/{feedId}/like")
  public ResponseEntity<FeedDto> likeFeed(@PathVariable UUID feedId) {
    likeService.likedFeed(feedId);
    return ResponseEntity.status(HttpStatus.OK).body(feedService.getFeed(feedId));
  }

  @DeleteMapping("/{feedId}/like")
  public ResponseEntity<FeedDto> unlikeFeed(@PathVariable UUID feedId) {
    likeService.unlikedFeed(feedId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping("/{feedId}/comments")
  public ResponseEntity<CommentDto> createComment(@PathVariable UUID feedId,
      @RequestBody @Valid CommentCreateRequest request) {
    CommentDto comment = commentService.createComment(feedId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(comment);
  }

  @GetMapping("/{feedId}/comments")
  public ResponseEntity<CommentDtoCursorResponse> listComments(@PathVariable UUID feedId,
      @RequestParam(required = false) Instant cursor, @RequestParam(required = false) UUID idAfter, @RequestParam int limit) {
    CommentDtoCursorResponse commentsByCursor = commentService.getCommentsByCursor(feedId, cursor,
        idAfter, limit);
    return ResponseEntity.status(HttpStatus.OK).body(commentsByCursor);
  }


}
