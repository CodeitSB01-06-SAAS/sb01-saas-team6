package com.codeit.sb01otbooteam06.domain.dm.service;

import com.codeit.sb01otbooteam06.domain.dm.dto.DirectMessageDto;
import com.codeit.sb01otbooteam06.domain.dm.entity.DirectMessage;
import com.codeit.sb01otbooteam06.domain.dm.repository.DirectMessageRepository;
import com.codeit.sb01otbooteam06.domain.user.entity.User;
import com.codeit.sb01otbooteam06.domain.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DirectMessageService {

    private final DirectMessageRepository dmRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /* 전송 */
    @Transactional
    public UUID send(UUID senderId, UUID receiverId, String content) {
        User sender = getUser(senderId);
        User receiver = getUser(receiverId);

        DirectMessage dm = dmRepository.save(DirectMessage.from(sender, receiver, content));

        // STOMP 브로드캐스트 – dmKey는 동적으로 생성
        String key = DirectMessage.generateKey(senderId, receiverId);
        messagingTemplate.convertAndSend("/sub/direct-messages_" + key, DirectMessageDto.from(dm));
        return dm.getId();
    }

    /* 대화 조회 */
    public Slice<DirectMessageDto> chat(UUID me, UUID you, UUID cursor, int size) {
        Pageable page = PageRequest.ofSize(size);
        List<DirectMessageDto> list = dmRepository.findChat(me, you, cursor, page)
            .stream()
            .map(DirectMessageDto::from)
            .toList();
        return new SliceImpl<>(list, page, list.size() == size);
    }

    private User getUser(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}