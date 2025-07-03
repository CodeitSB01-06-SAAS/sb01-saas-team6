package com.codeit.sb01otbooteam06.domain.clothes.repository;

import com.codeit.sb01otbooteam06.domain.clothes.entity.Clothes;
import com.codeit.sb01otbooteam06.domain.clothes.entity.QClothes;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class ClothesCustomRepositoryImpl implements ClothesCustomRepository {

  private final JPAQueryFactory queryFactory;

  /**
   * Retrieves a list of Clothes entities filtered by type and owner, supporting cursor-based pagination.
   *
   * Results are ordered by creation time in descending order. If both cursor and idAfter are provided, only entities created before the cursor timestamp are returned.
   *
   * @param cursor     the timestamp string used as a pagination cursor; if provided with idAfter, fetches records created before this time
   * @param idAfter    an additional cursor parameter used in conjunction with cursor for pagination
   * @param limit      the maximum number of Clothes entities to return
   * @param typeEqual  the type of Clothes to filter by
   * @param ownerId    the UUID of the Clothes owner to filter by
   * @return a list of Clothes entities matching the specified criteria
   */
  @Override
  public List<Clothes> findAllByCursor(String cursor, String idAfter, int limit, String typeEqual,
      UUID ownerId) {
    QClothes qClothes = QClothes.clothes;
    BooleanBuilder builder = new BooleanBuilder();

    //type, ownerId 1차 select
    builder.and(qClothes.type.eq(typeEqual));
    builder.and(qClothes.owner.id.eq(ownerId));

    //정렬: createAt 내림차순
    OrderSpecifier<?> orderSpecifier;
    orderSpecifier = qClothes.createdAt.desc();

    //커서존재할경우
    if (cursor != null && idAfter != null) {
      // 내림차순 정렬
      builder.and(qClothes.createdAt.lt(Instant.parse(cursor)));

    }

    List<Clothes> clothesList = queryFactory
        .selectFrom(qClothes)
        .where(builder)
        .orderBy(orderSpecifier)
        .limit(limit)
        .fetch();

    return clothesList;
  }


  /****
   * Returns the total number of Clothes entities for a given type and owner.
   *
   * @param typeEqual the type of clothes to filter by
   * @param ownerId the UUID of the owner to filter by
   * @return the count of Clothes entities matching the specified type and owner
   */
  @Override
  public int getTotalCounts(String typeEqual, UUID ownerId) {
    QClothes qClothes = QClothes.clothes;
    BooleanBuilder builder = new BooleanBuilder();

    //검색 조건
    builder.and(qClothes.type.eq(typeEqual));
    builder.and(qClothes.owner.id.eq(ownerId));

    return Math.toIntExact(queryFactory
        .select(qClothes.count())
        .from(qClothes)
        .where(builder)
        .fetchOne());

  }
}
