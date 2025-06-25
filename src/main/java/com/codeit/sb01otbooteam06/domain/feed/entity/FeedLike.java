package com.codeit.sb01otbooteam06.domain.feed.entity;

import com.codeit.sb01otbooteam06.domain.base.BaseUpdatableEntity;
import com.codeit.sb01otbooteam06.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feeds_likes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "feeds_likes_uk",
            columnNames = {"feed_id", "user_id"}
        )
    } // 복합키 써서, 피드에 사용자가 한번만 좋아요 누룰 수 있게 설정
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedLike extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "feed_id", nullable = false)
  private Feed feed; //연관된 피드 아이디

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user; //연관된 피드 아이디

  public FeedLike(Feed feed, User user) {
    this.feed = feed;
    this.user = user;
  }
}
