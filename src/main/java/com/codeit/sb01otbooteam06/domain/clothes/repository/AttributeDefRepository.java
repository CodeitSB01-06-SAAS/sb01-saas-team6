package com.codeit.sb01otbooteam06.domain.clothes.repository;

import com.codeit.sb01otbooteam06.domain.clothes.entity.AttributeDef;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeDefRepository extends JpaRepository<AttributeDef, UUID>,
    AttributeDefCustomRepository {

  /**
 * Checks if an AttributeDef entity with the specified name exists in the database.
 *
 * @param name the name of the attribute to check for existence
 * @return true if an AttributeDef with the given name exists, false otherwise
 */
boolean existsByName(String name);
}
