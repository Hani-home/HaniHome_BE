package org.hanihome.hanihomebe.property.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.deal.application.service.DealService;
import org.hanihome.hanihomebe.deal.repository.DealRepository;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.global.utility.SecurityContextUtils;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.metro.application.service.NearestMetroStopService;
import org.hanihome.hanihomebe.property.application.factory.PropertyFactory;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.property.domain.vo.ViewingAvailableDateTime;
import org.hanihome.hanihomebe.property.domain.enums.DisplayStatus;
import org.hanihome.hanihomebe.property.domain.enums.TradeStatus;
import org.hanihome.hanihomebe.property.domain.item.PropertyOptionItem;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.request.PropertyCompleteTradeDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.PropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.request.patch.PropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyWithMemberResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.TimeWithReserved;
import org.hanihome.hanihomebe.report.application.domain.ReportTargetType;
import org.hanihome.hanihomebe.report.service.ReportService;
import org.hanihome.hanihomebe.viewing.application.service.ViewingService;
import org.hanihome.hanihomebe.viewing.domain.ViewingStatus;
import org.hanihome.hanihomebe.viewing.repository.ViewingRepository;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.hanihome.hanihomebe.wishlist.repository.WishItemRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final MemberRepository memberRepository;
    private final OptionItemRepository optionItemRepository;
    private final WishItemRepository wishItemRepository; //Property 삭제 시 WishItem도 삭제하기 위해 추가
    private final NearestMetroStopService nearestMetroStopService;
    private final PropertyConversionService propertyConversionService;
    private final List<PropertyFactory> propertyFactories;
    private final DealService dealService;
    private final ViewingService viewingService;
    private final ViewingRepository viewingRepository;
    private final ReportService reportService;
    private final DealRepository dealRepository;


    /// create
    @Transactional
    public PropertyWithMemberResponseDTO createProperty(PropertyCreateRequestDTO dto, Long memberId){
        log.info("property 생성 로직 진입");

        Member findMember = memberRepository.findById(dto.memberId()).orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        Property property = dtoToEntity(dto, findMember);

        // TODO: 썸네일을 제대로 처리할 필요가있음
        property.setThumbnailUrl(dto.photoUrls() == null ? null : dto.photoUrls().get(0));
        addPropertyOptionItem(dto.optionItemIds(), property);

        nearestMetroStopService.create(property);

        propertyRepository.save(property);//RentProperty 테이블에도 JPA가 insert
        log.info("RentPrperty 생성 저장 성공");

        return propertyConversionService.convertProperty(property, PropertyViewType.DEFAULT);

    }

    private Property dtoToEntity(PropertyCreateRequestDTO dto, Member findMember) {
        PropertyFactory propertyFactory = propertyFactories.stream()
                .filter(factory -> factory.supports(dto))
                .findFirst()
                .orElseThrow(() -> new CustomException(ServiceCode.INVALID_PROPERTY_TYPE));
        Property property = propertyFactory.create(dto, findMember);
        return property;
    }


    /// read
    /**
     *  전체 Property 조회
     *    - 모든 서브타입(RentProperty, ShareProperty)을 섞어서 반환합니다.
     */
    public <T> List<T> getAllProperties(PropertyViewType view) {
        List<Property> findProperties = propertyRepository.findAll();

        List<Long> propertyIds = findProperties.stream()
                .map(Property::getId)
                .toList();


        return propertyConversionService.convertProperties(findProperties, view);
    }


    /**
     * 단일 Property 조회 (부모 타입으로 조회)
     */
    public PropertyWithMemberResponseDTO getPropertyById(Long id) {
        Property findProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ServiceCode.PROPERTY_NOT_EXISTS));
        if (guestApproachToHiddenProperty(findProperty)) {
            throw new CustomException(ServiceCode.PROPERTY_IS_HIDDEN);
        } else {
            return propertyConversionService.convertProperty(findProperty, PropertyViewType.DEFAULT);
        } 
    }

    private static boolean guestApproachToHiddenProperty(Property findProperty) {
        return findProperty.getDisplayStatus().equals(DisplayStatus.INACTIVE) && (!requesterIsPropertyOwner(findProperty));
    }

    /**
     * 회원 별 Property 조회
     * TradeStatus와 PropertyViewType을 인자로 받아 해당 조건에 맞는 매물을 조회하고 변환
     */
    public <T> List<T> getPropertiesByMemberId(Long memberId,
                                               TradeStatus tradeStatus,
                                               PropertyViewType view) {
        DisplayStatus displayStatus = chooseDisplayStatusByOwnership(memberId);

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        List<Property> findProperties = propertyRepository.findByMemberAndDisplayStatusAndTradeStatus(findMember, displayStatus, tradeStatus);

        return propertyConversionService.convertProperties(findProperties, view);
    }

    private static DisplayStatus chooseDisplayStatusByOwnership(Long propertyOwnerId) {
        DisplayStatus displayStatus;
        Optional<Long> optRequesterId = SecurityContextUtils.getHttpRequesterId();
        if (optRequesterId.isEmpty()) { // 비로그인 사용자
            displayStatus = DisplayStatus.ACTIVE;
        } else if (optRequesterId.get().equals(propertyOwnerId)) { // 소유자
            displayStatus = null;
        } else { // 로그인 & 비소유자
            displayStatus = DisplayStatus.ACTIVE;
        }
        return displayStatus;
    }

    /**
     * 내 Property 조회
     * */
    public <T> List<T> getMyProperty(Long memberId, TradeStatus tradeStatus, DisplayStatus displayStatus, PropertyViewType view) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));


        List<Property> findProperties = propertyRepository.findByMemberAndDisplayStatusAndTradeStatus(findMember, displayStatus, tradeStatus);

        return propertyConversionService.convertProperties(findProperties, view);
    }

    /**
     * 특정 매물의 뷰잉 가능 시각 조회
     */
    public Map<LocalDate, List<TimeWithReserved>> getViewingAvailableDateTimes(Long propertyId) {
        Property findProperty = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new CustomException(ServiceCode.PROPERTY_NOT_EXISTS));
        List<ViewingAvailableDateTime> dateTimes = findProperty.getViewingAvailableDateTimes();

        Map<LocalDate, List<TimeWithReserved>> response = groupByDate(dateTimes);
        sortingByTime(response);
        return response;
    }

    private static void sortingByTime(Map<LocalDate, List<TimeWithReserved>> response) {
        response.forEach((date, times) ->
                times.sort(Comparator.comparing(timeWithReserved -> timeWithReserved.time())));
    }

    private static TreeMap<LocalDate, List<TimeWithReserved>> groupByDate(List<ViewingAvailableDateTime> dateTimes) {
        return dateTimes.stream()
                .collect(Collectors.groupingBy(dateTime -> dateTime.getDate(),
                        TreeMap::new,
                        Collectors.mapping(dateTime ->
                                        new TimeWithReserved(dateTime.getTime(), dateTime.isReserved())
                                , Collectors.toList())));
    }

    /// update

    @Transactional
    public PropertyWithMemberResponseDTO patch(Long propertyId, PropertyPatchRequestDTO dto) {
        Property findProperty = propertyRepository.findById(propertyId).orElseThrow(() -> new RuntimeException("Property not found: " + propertyId));
        List<PropertyOptionItem> propertyOptionItems = dto.getOptionItemIds() == null ? null
                : createPropertyOptionItems(dto, findProperty);

        Property updated = findProperty.update(dto.toCommand(propertyOptionItems));
        return propertyConversionService.convertProperty(updated, PropertyViewType.DEFAULT);
    }

    private List<PropertyOptionItem> createPropertyOptionItems(PropertyPatchRequestDTO dto, Property findProperty) {
        return dto.getOptionItemIds().stream()
                .map(optionItemId ->
                        {
                            OptionItem optionItem = optionItemRepository.findById(optionItemId).orElseThrow(() -> new RuntimeException("해당하는 선택목록 식별자가 없습니다."));
                            PropertyOptionItem propertyOptionItem = PropertyOptionItem.builder()
                                    .property(findProperty)
                                    .optionItem(optionItem)
                                    .optionItemName(optionItem.getItemName())
                                    .build();
                            return propertyOptionItem;
                        }
                )
                .toList();
    }

    /// delete
    @Transactional
    public void deletePropertyById(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new CustomException(ServiceCode.PROPERTY_NOT_EXISTS);
        }
        if (propertyHasViewingsInREQUESTED(id)) {
            throw new CustomException(ServiceCode.PROPERTY_HAS_REQUESTED_VIEWINGS);
        }
        wishItemRepository.deleteAllByTargetTypeAndTargetId(WishTargetType.PROPERTY, id); //해당 찜하기 삭제
        viewingRepository.deleteByProperty_Id(id);
        dealRepository.deleteByProperty_Id(id);
        nearestMetroStopService.deleteByPropertyId(id);
        reportService.delete(id, ReportTargetType.PROPERTY);

        try {
            propertyRepository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new CustomException(ServiceCode.PROPERTY_DELETE_FAILED_HAS_RELATIONS, e);
        }
    }

    private boolean propertyHasViewingsInREQUESTED(Long id) {
        return viewingRepository.findByProperty_IdAndStatus(id, ViewingStatus.REQUESTED).size() > 0;
    }


    private void addPropertyOptionItem(List<Long> optionItemIds, Property property) {
        optionItemIds.forEach(optionItemId -> {
            OptionItem optionItem = optionItemRepository.findById(optionItemId).orElseThrow(() -> new RuntimeException("해당하는 선택목록 식별자가 없습니다."));
            PropertyOptionItem propertyOptionItem = PropertyOptionItem.builder()
                    .property(property)
                    .optionItem(optionItem)
                    .optionItemName(optionItem.getItemName())
                    .build();
            property.addPropertyOptionItem(propertyOptionItem);
        });
    }

    // 매물 거래 완료
    @Transactional
    public void completeTrade(PropertyCompleteTradeDTO dto) {
        Property findProperty = propertyRepository.findById(dto.propertyId())
                .orElseThrow(() -> new CustomException(ServiceCode.PROPERTY_NOT_EXISTS));

        validateRequesterIsOwner(findProperty);

        if (dto.dealWithOutsider()) {
            changeStatusAndCancelViewingsInREQUESTED(findProperty);
        } else {
            changeStatusAndCancelViewingsInREQUESTED(findProperty);
            createDealWithGuest(dto);
        }

    }

    private Long createDealWithGuest(PropertyCompleteTradeDTO dto) {
        // dto에서 전달받은 게스트에게만 구한매물로 취급
        return dealService.createDeal(dto.viewingId());
    }

    private void changeStatusAndCancelViewingsInREQUESTED(Property findProperty) {
        // 매물 거래 완료
        findProperty.completeTrade();
        // 매물에 연결된 나머지 REQUESTED 뷰잉은 모두 취소로 상태 변경
        viewingService.cancelViewingForCompletedProperty(findProperty.getId());
    }

    private static void validateRequesterIsOwner(Property findProperty) {
//        Optional<Long> optRequesterId = SecurityContextUtils.getHttpRequesterId();
        if (!requesterIsPropertyOwner(findProperty)) {
            throw new CustomException(ServiceCode.NO_OWNER_AUTHORITY);
        }
    }

    private static boolean requesterIsPropertyOwner(Property findProperty) {
        log.info("property owner:{}, requester:{}", findProperty.getMember().getId(), SecurityContextUtils.getHttpRequesterId().orElse(null));
        return !(SecurityContextUtils.getHttpRequesterId().isEmpty() || !(SecurityContextUtils.getHttpRequesterId().get().equals(findProperty.getMember().getId())));
    }
}