package com.codeit.sb01otbooteam06.domain.follow.dto;

import com.codeit.sb01otbooteam06.domain.follow.entity.Follow;
import java.util.UUID;

public record FollowDto(UUID id, UUID followerId, UUID followeeId) {

    public static FollowDto from(Follow f) {
        return new FollowDto(f.getId(), f.getFollower().getId(), f.getFollowee().getId());
    }
}
