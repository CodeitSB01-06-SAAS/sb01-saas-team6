package com.codeit.sb01otbooteam06.domain.follow.controller;

import com.codeit.sb01otbooteam06.domain.follow.dto.FollowDto;
import com.codeit.sb01otbooteam06.domain.follow.service.FollowService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    /** 팔로우 */
    @PostMapping("/{followeeId}")
    public FollowDto follow(@PathVariable UUID followeeId,
        @RequestHeader("X-USER-ID") UUID me) {
        return followService.follow(me, followeeId);
    }

    /** 언팔로우 */
    @DeleteMapping("/{followeeId}")
    public ResponseEntity<Void> unfollow(@PathVariable UUID followeeId,
        @RequestHeader("X-USER-ID") UUID me) {
        followService.unfollow(me, followeeId);
        return ResponseEntity.noContent().build();
    }

    /** 내 팔로잉 목록 */
    @GetMapping("/followings")
    public Slice<FollowDto> followings(@RequestParam(required = false) UUID cursor,
        @RequestParam(defaultValue = "20") int size,
        @RequestHeader("X-USER-ID") UUID me) {
        return followService.followings(me, cursor, size);
    }

    /** 나를 팔로우한 목록 */
    @GetMapping("/followers/{userId}")
    public Slice<FollowDto> followers(@PathVariable UUID userId,
        @RequestParam(required = false) UUID cursor,
        @RequestParam(defaultValue = "20") int size) {
        return followService.followers(userId, cursor, size);
    }
}
