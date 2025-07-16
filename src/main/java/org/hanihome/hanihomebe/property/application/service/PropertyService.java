package org.hanihome.hanihomebe.property.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.item.application.OptionItemConverterForProperty;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.metro.domain.MetroStop;
import org.hanihome.hanihomebe.metro.domain.NearestMetroStop;
import org.hanihome.hanihomebe.metro.repository.MetroStopRepository;
import org.hanihome.hanihomebe.metro.repository.NearestMetroStopRepository;
import org.hanihome.hanihomebe.property.application.converter.PropertyConverter;
import org.hanihome.hanihomebe.property.application.converter.PropertyMapper;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.metro.web.dto.nearest.NearestMetroStopProjectionDTO;
import org.hanihome.hanihomebe.property.domain.vo.ViewingAvailableDateTime;
import org.hanihome.hanihomebe.property.domain.enums.DisplayStatus;
import org.hanihome.hanihomebe.property.domain.enums.TradeStatus;
import org.hanihome.hanihomebe.property.domain.item.PropertyOptionItem;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.request.PropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.request.PropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.request.RentPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.request.SharePropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.TimeWithReserved;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.hanihome.hanihomebe.wishlist.repository.WishItemRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;
    private final PropertyMapper propertyMapper;
    private final Validator validator;
    private final OptionItemRepository optionItemRepository;
    private final WishItemRepository wishItemRepository; //Property 삭제 시 WishItem도 삭제하기 위해 추가
    private final MetroStopRepository metroStopRepository;
    private final NearestMetroStopRepository nearestMetroStopRepository;
    private final OptionItemConverterForProperty optionItemConverter;
    private final PropertyConversionService propertyConversionService;
    private final Map<PropertyViewType, PropertyConverter<?>> propertyConverterMap = new EnumMap<>(PropertyViewType.class);

    public PropertyService(PropertyRepository propertyRepository,
                           MemberRepository memberRepository,
                           ObjectMapper objectMapper,
                           PropertyMapper propertyMapper,
                           Validator validator,
                           OptionItemRepository optionItemRepository,
                           WishItemRepository wishItemRepository,
                           MetroStopRepository metroStopRepository,
                           NearestMetroStopRepository nearestMetroStopRepository,
                           OptionItemConverterForProperty optionItemConverter,
                           PropertyConversionService propertyConversionService,
                           List<PropertyConverter<?>> propertyConverters, ConversionService conversionService) {
        this.propertyRepository = propertyRepository;
        this.memberRepository = memberRepository;
        this.objectMapper = objectMapper;
        this.propertyMapper = propertyMapper;
        this.validator = validator;
        this.optionItemRepository = optionItemRepository;
        this.wishItemRepository = wishItemRepository;
        this.metroStopRepository = metroStopRepository;
        this.nearestMetroStopRepository = nearestMetroStopRepository;
        this.optionItemConverter = optionItemConverter;
        this.propertyConversionService = propertyConversionService;
        propertyConverters.forEach(converter ->
                propertyConverterMap.put(converter.supports(), converter));
    }

    //create
    /**
     * 1) RentProperty 생성 (부모 레포지토리로 저장)
     */
    @Transactional
    public PropertyResponseDTO createProperty(PropertyCreateRequestDTO dto){
        log.info("property 생성 로직 진입");
        Member findMember = memberRepository.findById(dto.memberId()).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));

        if(dto instanceof RentPropertyCreateRequestDTO){
            RentProperty rentProperty = RentProperty.create((RentPropertyCreateRequestDTO) dto, findMember);
            rentProperty.setThumbnailUrl(dto.photoUrls() == null ? null : dto.photoUrls().get(0));

            // TODO: nearestStation, property 생성 모두 단일책임하도록 분리하고, 별도의 orchestrator 사용이 필요할듯
            NearestMetroStop nearestMetroStop = createNearestMetroStop(dto, rentProperty);
            nearestMetroStopRepository.save(nearestMetroStop);

            // create PropertyOptionItem
            addPropertyOptionItem(dto.optionItemIds(), rentProperty);

            RentProperty save = propertyRepository.save(rentProperty);//RentProperty 테이블에도 JPA가 insert
            log.info("RentPrperty 생성 저장 성공");

            return propertyMapper.toResponseDTO(save, getOptionItemResponseDTOS(rentProperty));
        } else if (dto instanceof SharePropertyCreateRequestDTO){
            ShareProperty shareProperty = ShareProperty.create((SharePropertyCreateRequestDTO) dto, findMember);
            shareProperty.setThumbnailUrl(dto.photoUrls() == null ? null : dto.photoUrls().get(0));

            NearestMetroStop nearestMetroStop = createNearestMetroStop(dto, shareProperty);
            nearestMetroStopRepository.save(nearestMetroStop);

            // create PropertyOptionItem
            addPropertyOptionItem(dto.optionItemIds(), shareProperty);

            ShareProperty save = propertyRepository.save(shareProperty);
            log.info("SharaProperty 생성 저장 성공");

            return propertyMapper.toResponseDTO(save, getOptionItemResponseDTOS(shareProperty));
        }

        throw new CustomException(ServiceCode.INVALID_PROPERTY_TYPE);
    }

    private NearestMetroStop createNearestMetroStop(PropertyCreateRequestDTO dto, Property property) {
        NearestMetroStopProjectionDTO nearestMetroAndDistance = metroStopRepository.findNearestMetroAndDistance(dto.region().getLatitude(), dto.region().getLongitude());

        MetroStop findMetroStop = metroStopRepository.findById(nearestMetroAndDistance.getId()).orElseThrow(() -> new CustomException(ServiceCode.METRO_STOP_NOT_EXISTS));
        Double distance = nearestMetroAndDistance.getDistance();

        return NearestMetroStop.create(findMetroStop, property, distance);
    }


    // read
    /**
     * 3) 전체 Property 조회
     *    - 모든 서브타입(RentProperty, ShareProperty)을 섞어서 반환합니다.
     */
    public <T> List<T> getAllProperties(PropertyViewType view) {
        List<Property> findProperties = propertyRepository.findAll();

        return propertyConversionService.convertProperties(findProperties, view);
    }


    /**
     * 4) 단일 Property 조회 (부모 타입으로 조회)
     */
    public PropertyResponseDTO getPropertyById(Long id) {
        Property findProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found: " + id));

        return propertyMapper.toResponseDTO(findProperty, getOptionItemResponseDTOS(findProperty));
    }

    /**
     * 회원 별 Property 조회
     */
    /**
     * 회원 별 Property 조회
     * TradeStatus와 PropertyViewType을 인자로 받아 해당 조건에 맞는 매물을 조회하고 변환
     */
    public <T> List<T> getPropertiesByMemberId(Long memberId, CustomUserDetails userDetails, TradeStatus tradeStatus, PropertyViewType view) {
        DisplayStatus displayStatus = chooseDisplayStatusByOwnership(memberId, userDetails);

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        List<Property> findProperties = propertyRepository.findByMemberAndDisplayStatusAndTradeStatus(findMember, displayStatus, tradeStatus);


        return propertyConversionService.convertProperties(findProperties, view);
    }

    private static DisplayStatus chooseDisplayStatusByOwnership(Long memberId, CustomUserDetails userDetails) {
        DisplayStatus displayStatus;
        if (userDetails == null) {                        // 일반 사용자별 매물 조회
            displayStatus = DisplayStatus.ACTIVE;
        } else if (userDetails.getUserId() == memberId) { // 매물 소유자
            displayStatus = null;
        } else {
            displayStatus = DisplayStatus.ACTIVE;
        }
        return displayStatus;
    }

    /**
     * 내 Property 조회
     * */
//    public <T> List<T> getMyProperty(Long memberId, PropertyViewType view) {
//        Member findMember = memberRepository.findById(memberId)
//                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));
//
//        PropertyConverter<T> converter = (PropertyConverter<T>) getConverterByView(view);
//
//        return propertyRepository.findByMemberAndDisplayStatus(findMember, null).stream()
//                .map(property -> converter.convert(property, getOptionItemResponseDTOS(property)))
//                .collect(Collectors.toList());
//    }
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

        Map<LocalDate, List<TimeWithReserved>> response = dateTimes.stream()
                .collect(Collectors.groupingBy(dateTime -> dateTime.getDate(),
                        TreeMap::new,
                        Collectors.mapping(dateTime -> {
                    return new TimeWithReserved(dateTime.getTime(), dateTime.isReserved());
                }, Collectors.toList())));
        response.forEach((date, times) ->
                times.sort(Comparator.comparing(timeWithReserved -> timeWithReserved.time())));
        return response;
    }


    //update
    /* json-patch(매물이 더 복잡해질 경우 고려 가능)
    @Transactional
    public PropertyResponseDTO patch(Long propertyId, JsonNode patchDocument) throws JsonPatchException, IOException {
        Property findProperty = propertyRepository.findById(propertyId).orElseThrow(() -> new RuntimeException("Property not found: " + propertyId));

        if(findProperty instanceof RentProperty entity) {
            // ① 스냅샷 → JsonNode
            RentPropertyPatchRequestDTO dtoSnapshot = propertyMapper.toPatchDTO(entity);
            JsonNode original = objectMapper.valueToTree(dtoSnapshot);

            // ② JSON Patch 적용
            JsonPatch patch = JsonPatch.fromJson(patchDocument);
            JsonNode patchedNode = patch.apply(original);
            RentPropertyPatchRequestDTO patchedDto =
                    objectMapper.treeToValue(patchedNode, RentPropertyPatchRequestDTO.class);   // 해당 DTO에 null값은 없음. 이유: 원본 스냅샷을 DTO로 변환한 것에 patch를 적용했으므로.
                                                                                                // 만약 remove OP를 해서 키가 없는경우, ObjectMapper는 null을 넣어준다
            // ③ (옵션) Bean Validation
//            validator.validate(patchedDto);

            // ④ 엔티티 부분 업데이트
            propertyMapper.applyPatch(patchedDto, entity);

            // ⑤ 응답 변환
            return propertyMapper.toResponseDto(entity);
        } else if (findProperty instanceof ShareProperty entity) {
            // ① 스냅샷 → JsonNode
            SharePropertyPatchRequestDTO dtoSnapshot = propertyMapper.toPatchDTO(entity);
            JsonNode original = objectMapper.valueToTree(dtoSnapshot);

            // ② JSON Patch 적용
            JsonPatch patch = JsonPatch.fromJson(patchDocument);
            JsonNode patchedNode = patch.apply(original);
            SharePropertyPatchRequestDTO patchedDto =
                    objectMapper.treeToValue(patchedNode, SharePropertyPatchRequestDTO.class);

            // ③ (옵션) Bean Validation
//            validator.validate(patchedDto);

            // ④ 엔티티 부분 업데이트
            propertyMapper.applyPatch(patchedDto, entity);

            // ⑤ 응답 변환
            return propertyMapper.toResponseDto(findProperty);
        }

        throw new RuntimeException("해당하는 매물 종류가 없습니다");
    }
    */

    @Transactional
    public PropertyResponseDTO patch(Long propertyId, PropertyPatchRequestDTO dto) {
        Property findProperty = propertyRepository.findById(propertyId).orElseThrow(() -> new RuntimeException("Property not found: " + propertyId));
        List<PropertyOptionItem> propertyOptionItems = dto.getOptionItemsIds() == null ? null
                : createPropertyOptionItems(dto, findProperty);

        Property updated = findProperty.update(dto.toCommand(propertyOptionItems));
        return propertyMapper.toResponseDTO(updated, getOptionItemResponseDTOS(updated));
    }

    private List<PropertyOptionItem> createPropertyOptionItems(PropertyPatchRequestDTO dto, Property findProperty) {
        return dto.getOptionItemsIds().stream()
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

    private List<OptionItemResponseDTO> getOptionItemResponseDTOS(Property property) {
        List<OptionItemResponseDTO> optionItemsDTOs = optionItemConverter.toResponseDTO(property.getOptionItems());
        return optionItemsDTOs;
    }


    /**
     * 6) 삭제 예시
     */
    @Transactional
    public void deletePropertyById(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new RuntimeException("Property not found: " + id);
        }
        wishItemRepository.deleteAllByTargetTypeAndTargetId(WishTargetType.PROPERTY, id); //해당 찜하기 삭제

        propertyRepository.deleteById(id);
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

 /*   public <T> List<T> findByMemberIdAndDisplayStatus(Long memberId, DisplayStatus displayStatus, PropertyViewType view) {
        PropertyConverter<T> converter = (PropertyConverter<T>) getConverterByView(view);

        return propertyRepository.findByMember_IdAndDisplayStatus(memberId, displayStatus)
                .stream()
                .map(property -> converter.convert(property, getOptionItemResponseDTOS(property)))
                .toList();

    }*/
}