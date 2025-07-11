package org.hanihome.hanihomebe.wishlist.application;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.hanihome.hanihomebe.wishlist.repository.WishItemRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//호주시간 새벽 4시마다 각 매물 id로 WishItem의 찜 수 조회하여 업데이트
@Component
@RequiredArgsConstructor
public class WishCountSyncScheduler {

    private final WishItemRepository wishItemRepository;
    private final PropertyRepository propertyRepository;

    
    @Scheduled(cron = "0 0 17 * * *", zone = "UTC")
    @Transactional
    public void syncPropertyWishCounts() {
        List<Long> propertyIds = propertyRepository.findAllIds();

        for (Long propertyId : propertyIds) {
            long count = wishItemRepository.countByTargetTypeAndTargetId(WishTargetType.PROPERTY, propertyId);
            propertyRepository.updateWishCount(propertyId, count);
        }
    }

}
