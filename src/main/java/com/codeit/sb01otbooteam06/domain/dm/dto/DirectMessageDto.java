package com.codeit.sb01otbooteam06.domain.dm.dto;

import com.codeit.sb01otbooteam06.domain.dm.entity.DirectMessage;
import java.time.Instant;
import java.util.UUID;

public record DirectMessageDto(UUID id, UUID senderId, UUID receiverId,
                               String content, Instant createdAt) {

    public static DirectMessageDto from(DirectMessage dm) {
        return new DirectMessageDto(dm.getId(), dm.getSender().getId(), dm.getReceiver().getId(),
            dm.getContent(), dm.getCreatedAt());
    }
}
