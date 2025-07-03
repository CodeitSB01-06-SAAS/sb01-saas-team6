package com.codeit.sb01otbooteam06.domain.clothes.repository;

import com.codeit.sb01otbooteam06.domain.clothes.entity.AttributeDef;
import java.util.List;

public interface AttributeDefCustomRepository {

  /**
       * Retrieves a list of AttributeDef entities using cursor-based pagination, with optional sorting and keyword filtering.
       *
       * @param cursor the pagination cursor indicating the starting point for retrieval
       * @param idAfter the ID after which to start fetching results
       * @param i the maximum number of entities to retrieve
       * @param sortBy the field by which to sort the results
       * @param sortDirection the direction of sorting ("asc" or "desc")
       * @param keywordLike a keyword pattern to filter the results
       * @return a list of AttributeDef entities matching the specified criteria
       */
      List<AttributeDef> findAllByCursor(String cursor, String idAfter, int i, String sortBy,
      String sortDirection, String keywordLike);

  /**
 * Returns the total number of AttributeDef entities matching the specified sorting and keyword filter criteria.
 *
 * @param sortBy the field by which to sort the results
 * @param keywordLike the keyword pattern to filter the entities
 * @return the total count of matching AttributeDef entities
 */
int getTotalCounts(String sortBy, String keywordLike);
}
