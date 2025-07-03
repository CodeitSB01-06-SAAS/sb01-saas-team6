package com.codeit.sb01otbooteam06.domain.clothes.repository;

import com.codeit.sb01otbooteam06.domain.clothes.entity.Clothes;
import java.util.List;
import java.util.UUID;

public interface ClothesCustomRepository {

  /**
       * Retrieves a list of Clothes entities using cursor-based pagination, filtered by type and owner.
       *
       * @param cursor    the pagination cursor indicating the starting point for retrieval
       * @param idAfter   the ID after which to start fetching records
       * @param i         the maximum number of Clothes entities to return
       * @param typeEqual the type of Clothes to filter by
       * @param ownerId   the unique identifier of the owner
       * @return a list of Clothes entities matching the specified criteria
       */
      List<Clothes> findAllByCursor(String cursor, String idAfter, int i, String typeEqual,
      UUID ownerId);

  /**
 * Returns the total number of clothes records matching the specified type and owner.
 *
 * @param typeEqual the type of clothes to filter by
 * @param ownerId the unique identifier of the owner
 * @return the total count of matching clothes records
 */
int getTotalCounts(String typeEqual, UUID ownerId);
}
