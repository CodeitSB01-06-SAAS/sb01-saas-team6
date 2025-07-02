package com.codeit.sb01otbooteam06.domain.clothes.repository;

import com.codeit.sb01otbooteam06.domain.clothes.entity.AttributeDef;
import java.util.List;

public class AttributeDefCustomRepositoryImpl implements AttributeDefCustomRepository {

  @Override
  public List<AttributeDef> findAllByCursor(String cursor, String idAfter, int i, String sortBy,
      String sortDirection, String keywordLike) {
    return List.of();
  }

  @Override
  public int getTotalCounts(String sortBy, String keywordLike) {

    return 0;
  }
}
