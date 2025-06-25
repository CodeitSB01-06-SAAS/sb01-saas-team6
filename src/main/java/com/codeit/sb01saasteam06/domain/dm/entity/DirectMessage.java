package com.codeit.sb01saasteam06.domain.dm.entity;


import com.codeit.sb01saasteam06.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "direct_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DirectMessage extends BaseEntity {

    @Column(name = "sender_id", nullable = false, columnDefinition = "UUID")
    private UUID senderId;

    @Column(name = "receiver_id", nullable = false, columnDefinition = "UUID")
    private UUID receiverId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /* ── 정적 팩터리 ── */
    private DirectMessage(UUID senderId, UUID receiverId, String content) {
        this.senderId  = senderId;
        this.receiverId = receiverId;
        this.content    = content;
    }

    public static DirectMessage from(UUID senderId, UUID receiverId, String content) {
        return new DirectMessage(senderId, receiverId, content);
    }
}
