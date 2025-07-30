package org.hanihome.hanihomebe.viewing.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.viewing.domain.QViewing;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.domain.ViewingStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hanihome.hanihomebe.viewing.domain.QViewing.viewing;

@RequiredArgsConstructor
@Repository
public class CustomViewingRepositoryImpl implements CustomViewingRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Viewing> findByPropertyAndStatusList(Long propertyId, List<ViewingStatus> statusList) {
        BooleanBuilder statusCond = new BooleanBuilder();
        if (statusList != null && !statusList.isEmpty()) {
            statusList.forEach(status -> statusCond.or(viewing.status.eq(status)));
        }

        return queryFactory.selectFrom(viewing)
                .where(statusCond, viewing.property.id.eq(propertyId))
                .fetch();
    }
}
