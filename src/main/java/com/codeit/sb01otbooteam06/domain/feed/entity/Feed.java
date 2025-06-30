package com.codeit.sb01otbooteam06.domain.feed.entity;

import com.codeit.sb01otbooteam06.domain.base.BaseUpdatableEntity;
import com.codeit.sb01otbooteam06.domain.user.entity.User;
import com.codeit.sb01otbooteam06.domain.weather.entity.Weather;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feeds")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseUpdatableEntity {

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private Long likeCount = 0L;

  @Column(nullable = false)
  private Integer commentCount = 0;

  @Transient
  private boolean likedByMe;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "weather_id", nullable = false)
  private Weather weather;


  @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();


  public Feed(String content, Long likeCount, Integer commentCount, boolean likedByMe, User user,
      Weather weather) {
    this.content = content;
    this.likeCount = likeCount;
    this.commentCount = commentCount;
    this.likedByMe = likedByMe;
    this.user = user;
    this.weather = weather;
  }

  public void update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
  }

  public void addComment(Comment comment) {
    this.comments.add(comment);
    comment.setFeed(this);
    this.commentCount++;
  }

  public void removeComment(Comment comment) {
    this.comments.remove(comment);
    comment.setFeed(null);
    this.commentCount--;
  }

  public void like() {
    this.likeCount++;
  }

  public void unlike() {
    this.likeCount--;
  }

}
