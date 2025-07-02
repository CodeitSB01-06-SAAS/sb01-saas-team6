package com.codeit.sb01otbooteam06.domain.clothes.repository;

import com.codeit.sb01otbooteam06.domain.clothes.entity.Clothes;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClothesRepository extends JpaRepository<Clothes, UUID>, ClothesCustomRepository {
  //Jpa쿼리메소드


  @Query("SELECT c FROM Clothes c JOIN FETCH c.owner WHERE c.id = :id")
  Optional<Clothes> findByIdWithOwner(@Param("id") UUID id);

}
