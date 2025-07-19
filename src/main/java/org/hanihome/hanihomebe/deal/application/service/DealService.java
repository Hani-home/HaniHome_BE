package org.hanihome.hanihomebe.deal.application.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.deal.application.strategy.DealQueryStrategy;
import org.hanihome.hanihomebe.deal.domain.Deal;
import org.hanihome.hanihomebe.deal.domain.DealerType;
import org.hanihome.hanihomebe.deal.repository.DealRepository;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.application.service.PropertyConversionService;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.response.summary.PropertySummaryDTO;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.repository.ViewingRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
@Service
public class DealService {

    private final DealRepository dealRepository;
    private final ViewingRepository viewingRepository;
    private final Map<DealerType, DealQueryStrategy> dealQueryStrategyMap = new HashMap<>();
    private final PropertyConversionService propertyConversionService;
    private final ConversionService conversionService;

    public DealService(DealRepository dealRepository,
                       ViewingRepository viewingRepository,
                       List<DealQueryStrategy> dealQueryStrategies,
                       PropertyConversionService propertyConversionService,
                       ConversionService conversionService) {
        this.dealRepository = dealRepository;
        this.viewingRepository = viewingRepository;
        dealQueryStrategies.forEach(strategy ->
                dealQueryStrategyMap.put(strategy.supports(), strategy));
        this.propertyConversionService = propertyConversionService;
        this.conversionService = conversionService;
    }

    /**
     * 뷰잉 확정 후 거래(Deal) 생성
     * @param viewingId 거래 대상 뷰잉의 ID
     * @return 생성된 Deal의 ID
     */
    @Transactional
    public Long createDeal(Long viewingId) {
        Viewing findViewing = viewingRepository.findById(viewingId)
                .orElseThrow(() -> new CustomException(ServiceCode.VIEWING_NOT_EXISTS));
        Property findProperty = findViewing.getProperty();
        Member host = findProperty.getMember();
        Member guest = findViewing.getMember();

        Deal deal = Deal.create(
                findViewing,
                findProperty,
                host,
                guest
        );
        dealRepository.save(deal);
        return deal.getId();
    }

    public List<PropertySummaryDTO> getDealsByDealerType(Long requesterId, DealerType dealerType) {
        DealQueryStrategy queryStrategy = dealQueryStrategyMap.get(dealerType);

        List<Deal> findDeals = queryStrategy.findDeals(requesterId);

        return propertyConversionService.convertProperties(findDeals.stream().map(Deal::getProperty).toList(), PropertyViewType.SUMMARY);
    }


}
