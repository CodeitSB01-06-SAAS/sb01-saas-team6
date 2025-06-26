package com.codeit.sb01otbooteam06.domain.feed.service;

import com.codeit.sb01otbooteam06.domain.feed.dto.response.FeedDto;
import com.codeit.sb01otbooteam06.domain.feed.dto.request.CommentCreateRequest;

public interface CommentService {

  FeedDto createComment(CommentCreateRequest request);



}
