package com.codeit.sb01otbooteam06.domain.feed.dto.response;

import com.codeit.sb01otbooteam06.domain.feed.entity.Comment;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDto {

  private UUID id;
  private Instant createdAt;
  private UUID feedId;
  private AuthorDto author;
  private String content;

  public CommentDto(UUID id, Instant createdAt, UUID feedId, AuthorDto author, String content) {
    this.id = id;
    this.createdAt = createdAt;
    this.feedId = feedId;
    this.author = author;
    this.content = content;
  }

  public static CommentDto fromEntity(Comment comment) {
    // todo : 이부분 변환 클래스 나중에 변경하든가, 그대로 쓰던가 해야함.
    AuthorDto author = AuthorDto.fromEntity(comment.getUser());

    return new CommentDto(
        comment.getId(),
        comment.getCreatedAt(),
        comment.getFeed().getId(),
        author,
        comment.getContent()
    );
  }
}
