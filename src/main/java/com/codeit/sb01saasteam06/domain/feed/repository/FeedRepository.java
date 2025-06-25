package com.codeit.sb01saasteam06.domain.feed.repository;

import com.codeit.sb01saasteam06.domain.feed.entity.Feed;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, UUID> {


}
