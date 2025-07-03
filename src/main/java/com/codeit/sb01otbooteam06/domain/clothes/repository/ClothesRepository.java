package com.codeit.sb01otbooteam06.domain.clothes.repository;

import com.codeit.sb01otbooteam06.domain.clothes.entity.Clothes;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClothesRepository extends JpaRepository<Clothes, UUID>, ClothesCustomRepository {
  /**
   * Retrieves a {@link Clothes} entity by its ID, eagerly loading its associated owner in a single query.
   *
   * @param id the unique identifier of the clothes entity
   * @return an {@link Optional} containing the clothes entity with its owner if found, or empty if not found
   */


  @Query("SELECT c FROM Clothes c JOIN FETCH c.owner WHERE c.id = :id")
  Optional<Clothes> findByIdWithOwner(@Param("id") UUID id);

}
