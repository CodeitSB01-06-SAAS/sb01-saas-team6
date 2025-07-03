package com.codeit.sb01otbooteam06.domain.clothes.repository;

import com.codeit.sb01otbooteam06.domain.clothes.entity.Clothes;
import com.codeit.sb01otbooteam06.domain.clothes.entity.ClothesAttribute;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesAttributeRepository extends JpaRepository<ClothesAttribute, UUID> {

  /**
 * Deletes all ClothesAttribute entities associated with the specified Clothes entity.
 *
 * @param clothes the Clothes entity whose associated attributes will be deleted
 */
void deleteByClothes(Clothes clothes);

  /**
 * Retrieves all ClothesAttribute entities associated with the specified Clothes entity.
 *
 * @param clothes the Clothes entity whose attributes are to be retrieved
 * @return a list of ClothesAttribute entities linked to the given Clothes entity
 */
List<ClothesAttribute> findByClothes(Clothes clothes);
}
