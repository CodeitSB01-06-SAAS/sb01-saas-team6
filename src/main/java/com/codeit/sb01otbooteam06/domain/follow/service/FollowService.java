package com.codeit.sb01otbooteam06.domain.follow.service;

import com.codeit.sb01otbooteam06.domain.follow.dto.FollowDto;
import com.codeit.sb01otbooteam06.domain.follow.entity.Follow;
import com.codeit.sb01otbooteam06.domain.follow.repository.FollowRepository;
import com.codeit.sb01otbooteam06.domain.user.entity.User;
import com.codeit.sb01otbooteam06.domain.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public FollowDto follow(UUID followerId, UUID followeeId) {
        User follower = getUser(followerId);
        User followee = getUser(followeeId);

        if (followRepository.existsByFollowerAndFollowee(follower, followee)) {
            throw new IllegalStateException("이미 팔로우 중입니다.");
        }
        Follow follow = followRepository.save(Follow.from(follower, followee));
        return FollowDto.from(follow);
    }

    @Transactional
    public void unfollow(UUID followerId, UUID followeeId) {
        User follower = getUser(followerId);
        User followee = getUser(followeeId);
        followRepository.deleteByFollowerAndFollowee(follower, followee);
    }

    public Slice<FollowDto> followers(UUID followeeId, UUID cursor, int size) {
        Pageable page = PageRequest.ofSize(size);
        List<FollowDto> list = followRepository.findFollowers(followeeId, cursor, page)
            .stream()
            .map(FollowDto::from)
            .toList();
        return new SliceImpl<>(list, page, list.size() == size);
    }

    public Slice<FollowDto> followings(UUID followerId, UUID cursor, int size) {
        Pageable page = PageRequest.ofSize(size);
        List<FollowDto> list = followRepository.findFollowings(followerId, cursor, page)
            .stream()
            .map(FollowDto::from)
            .toList();
        return new SliceImpl<>(list, page, list.size() == size);
    }

    private User getUser(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
