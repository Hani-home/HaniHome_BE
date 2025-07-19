package org.hanihome.hanihomebe.deal.application.strategy;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.deal.domain.Deal;
import org.hanihome.hanihomebe.deal.domain.DealerType;
import org.hanihome.hanihomebe.deal.repository.DealRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class HostDealQueryStrategy implements DealQueryStrategy {
    private final DealRepository dealRepository;

    @Override
    public DealerType supports() {
        return DealerType.DEAL_AS_HOST;
    }

    @Override
    public List<Deal> findDeals(Long memberId) {
        return dealRepository.findByHost_Id(memberId);
    }
}
