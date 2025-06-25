package com.codeit.sb01otbooteam06.domain.profile.repository;

import com.codeit.sb01otbooteam06.domain.profile.entity.Profile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {

  Optional<Profile> findById(UUID id);
}
