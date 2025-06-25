package com.codeit.sb01saasteam06.domain.dm.repository;

import com.codeit.sb01saasteam06.domain.dm.entity.DirectMessage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, UUID> {

    @Query("""
        SELECT dm
          FROM DirectMessage dm
         WHERE ((dm.senderId   = :me AND dm.receiverId = :you)
             OR (dm.senderId   = :you AND dm.receiverId = :me))
           AND (:cursor IS NULL OR dm.id < :cursor)
      ORDER BY dm.id DESC
    """)
    List<DirectMessage> findChat(UUID me, UUID you, UUID cursor, Pageable page);
}
