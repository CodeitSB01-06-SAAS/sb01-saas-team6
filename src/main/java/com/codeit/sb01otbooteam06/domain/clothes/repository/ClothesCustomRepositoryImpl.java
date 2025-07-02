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
   * 검색 조건에 따른 의상 목록을 반환합니다.
   *
   * @param cursor
   * @param idAfter
   * @param limit
   * @param typeEqual
   * @param ownerId
   * @return 의상 목록
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


  /**
   * 사용자의 의상 타입에 따른 의상 개수를 반환한다.
   *
   * @param typeEqual
   * @param ownerId
   * @return 사용자의 의상 타입에 따른 의상 개수
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
