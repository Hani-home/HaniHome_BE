package org.hanihome.hanihomebe.deal.application.strategy;

import com.amazonaws.util.LengthCheckInputStream;
import org.hanihome.hanihomebe.deal.domain.Deal;
import org.hanihome.hanihomebe.deal.domain.DealerType;

import java.util.List;

public interface DealQueryStrategy {
    DealerType supports();

    List<Deal> findDeals(Long memberId);
}
