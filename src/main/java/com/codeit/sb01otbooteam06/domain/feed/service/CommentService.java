package com.codeit.sb01otbooteam06.domain.feed.service;

import com.codeit.sb01otbooteam06.domain.feed.dto.response.CommentDtoCursorResponse;
import com.codeit.sb01otbooteam06.domain.feed.dto.response.FeedDto;
import com.codeit.sb01otbooteam06.domain.feed.dto.request.CommentCreateRequest;
import com.codeit.sb01otbooteam06.domain.feed.dto.response.FeedDtoCursorResponse;
import java.time.Instant;
import java.util.UUID;

public interface CommentService {

  FeedDto createComment(CommentCreateRequest request);

  CommentDtoCursorResponse getCommentsByCursor(UUID feedId, Instant cursor, UUID idAfter,
      int limit);

}
