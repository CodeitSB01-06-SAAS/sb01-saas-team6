package com.codeit.sb01saasteam06.domain.clothes.repository;

import com.codeit.sb01saasteam06.domain.clothes.entity.AttributesDef;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributesDefRepository extends JpaRepository<AttributesDef, UUID> {

}
