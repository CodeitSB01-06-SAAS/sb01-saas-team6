package com.codeit.sb01otbooteam06.domain.dm.controller;

import com.codeit.sb01otbooteam06.domain.dm.dto.DirectMessageCreateRequest;
import com.codeit.sb01otbooteam06.domain.dm.dto.DirectMessageDto;
import com.codeit.sb01otbooteam06.domain.dm.service.DirectMessageService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/direct-messages")
@RequiredArgsConstructor
public class DirectMessageController {
    private final DirectMessageService dmService;

    /** 메시지 전송 */
    @PostMapping
    public ResponseEntity<UUID> send(@RequestBody @Valid DirectMessageCreateRequest req,
        @RequestHeader("X-USER-ID") UUID senderId) {
        UUID id = dmService.send(senderId, req.receiverId(), req.content());
        return ResponseEntity.ok(id);
    }

    /** 채팅 페이징 조회 */
    @GetMapping("/{withUserId}")
    public Slice<DirectMessageDto> chat(@PathVariable UUID withUserId,
        @RequestParam(required = false) UUID cursor,
        @RequestParam(defaultValue = "20") int size,
        @RequestHeader("X-USER-ID") UUID me) {
        return dmService.chat(me, withUserId, cursor, size);
    }
}
