package org.hanihome.hanihomebe.property.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.QProperty;
import org.hanihome.hanihomebe.property.domain.QRentProperty;
import org.hanihome.hanihomebe.property.domain.QShareProperty;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.enums.RentPropertySubType;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.web.dto.PropertySearchConditionDTO;
import org.springframework.stereotype.Repository;
import java.lang.*;
import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PropertySearchRepositoryImpl implements PropertySearchRepository {
    private final JPAQueryFactory queryFactory;

    /**
     * 필터링
     * 필터링 기준: 매물 종류, 매물 유형, 1주당 예산 범위, 빌포함, 입주가능일, 즉시입주가능, 협의 가능, 지하철역부터 Nkm
     * @param cond 필터링 기준
     * @return 필터링된 매물 리스트
     */
    @Override
    public List<Property> search(PropertySearchConditionDTO cond) {
        // query dsl
        QProperty p = QProperty.property;
        QShareProperty sp = QShareProperty.shareProperty;
        QRentProperty rp = QRentProperty.rentProperty;

        BooleanBuilder baseBuilder = new BooleanBuilder();

        // 매물 종류
        List<PropertySuperType> kinds = cond.getKinds();
        if(kinds !=null && !cond.getKinds().isEmpty()){
            baseBuilder.and((p.kind.in(cond.getKinds())));

            BooleanBuilder subtypeBuilder = new BooleanBuilder();
            // 매물 유형
            if (cond.getSharePropertySubTypes() != null && !cond.getSharePropertySubTypes().isEmpty()) {
                subtypeBuilder.or(sp.sharePropertySubType.in(cond.getSharePropertySubTypes()));
            }else {
                subtypeBuilder.or(sp.sharePropertySubType.in(SharePropertySubType.values()));
            }
            if (cond.getRentPropertySubTypes() != null && !cond.getRentPropertySubTypes().isEmpty()) {
                subtypeBuilder.or(rp.rentPropertySubType.in(cond.getRentPropertySubTypes()));
            } else {
                subtypeBuilder.or(rp.rentPropertySubType.in(RentPropertySubType.values()));
            }
            baseBuilder.and(subtypeBuilder);
        }

        // 1주 당 예산 범위
        // TODO: 논의 사항 존재. 매물 등록시에 적은 요금이 (빌포함 요금, 빌 미포함 요금) 이 두가지 요금을 가지고 있어야하는게 아닌가..
        //  지금은 조건에 빌포함을 누르면, 빌포함을 체크한 매물만 조회됨
        if (cond.getMinWeeklyCost() != null && cond.getMaxWeeklyCost() != null) {
            baseBuilder.and(p.weeklyCost.between(cond.getMinWeeklyCost(), cond.getMaxWeeklyCost()));
        }

        // 빌포함
        if(cond.getBillIncluded() != null){
            baseBuilder.and(p.isBillIncluded.eq(cond.getBillIncluded()));
        }

        // 입주 가능일
        BooleanBuilder availableBuilder = new BooleanBuilder();
        if (cond.getAvailableFrom() != null && cond.getAvailableTo() != null) {
            availableBuilder.and(p.availableFrom.loe(cond.getAvailableTo()));
            availableBuilder.and(p.availableTo.goe(cond.getAvailableFrom()));
        }
        baseBuilder.and(availableBuilder);

        // 즉시 입주가능, 협의 가능
        if (cond.getImmediate() != null) {
            baseBuilder.and(p.isImmediate.eq(cond.getImmediate()));
        }
        if (cond.getNegotiable() != null) {
            baseBuilder.and(p.isNegotiable.eq(cond.getNegotiable()));
        }

        // 지하철역 기준 Nkm 필터
        // TODO: 품질보증: 오차 범위가 어떻게되는지 확인이 필요
        if (
                cond.getMetroStopLatitude() != null
                && cond.getMetroStopLongitude() != null
                && cond.getRadiusKm() != null
        ) {
            // km => m
            BigDecimal radiusMeters = cond.getRadiusKm().multiply(BigDecimal.valueOf(1000));
            BooleanExpression metroFilter = Expressions.booleanTemplate(
                    "ST_Distance_Sphere(" +
                            "ST_GeomFromText('POINT(' || {0} || ' ' || {1} || ')', 4326), " +   // property point
                            "ST_GeomFromText('POINT(' || {2} || ' ' || {3} || ')', 4326)" +     // metro point
                            ") <= {4}",
                    p.region.longitude,
                    p.region.latitude,
                    cond.getMetroStopLongitude(),
                    cond.getMetroStopLatitude(),
                    radiusMeters
            );

            baseBuilder.and(metroFilter);
        }

        JPAQuery<Property> query = queryFactory
                .selectFrom(p)
                .leftJoin(sp).on(p.id.eq(sp.id))
                .leftJoin(rp).on(p.id.eq(rp.id))
                .where(baseBuilder);
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//        List<Property> content = query.fetch();
//        long total = quQery.fetchCount();
//        return new PageImpl<>(content, pageable, total);
        List<Property> findProperties = query.fetch();
        return findProperties;
    }

}
