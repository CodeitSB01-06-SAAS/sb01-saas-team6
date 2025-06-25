package com.codeit.sb01otbooteam06.domain.feed.repository;

import com.codeit.sb01otbooteam06.domain.feed.entity.Comment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {


}
