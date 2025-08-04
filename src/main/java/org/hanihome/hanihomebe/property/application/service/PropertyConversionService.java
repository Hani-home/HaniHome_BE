package org.hanihome.hanihomebe.property.application.service;// PropertyConversionService.java (이전 답변에서 제공된 코드)

import io.opencensus.stats.Measure;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.global.utility.SecurityContextUtils;
import org.hanihome.hanihomebe.item.application.converter.OptionItemConverterForProperty;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.member.web.dto.MemberSummaryDTO;
import org.hanihome.hanihomebe.metro.domain.NearestMetroStop;
import org.hanihome.hanihomebe.metro.repository.NearestMetroStopRepository;
import org.hanihome.hanihomebe.metro.web.dto.nearest.NearestMetroStopResponseDTO;
import org.hanihome.hanihomebe.property.application.converter.PropertyConvertContext;
import org.hanihome.hanihomebe.property.application.converter.PropertyConverter;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.response.summary.MetaInfo;
import org.hanihome.hanihomebe.wishlist.domain.WishItem;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.hanihome.hanihomebe.wishlist.repository.WishItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PropertyConversionService {

    private final Map<PropertyViewType, PropertyConverter<?>> propertyConverterMap;
    private final NearestMetroStopRepository nearestMetroStopRepository;
    private final OptionItemConverterForProperty optionItemConverter;
    private final MemberRepository memberRepository;
    private final WishItemRepository wishItemRepository;

    public PropertyConversionService(List<PropertyConverter<?>> converterList,
                                     NearestMetroStopRepository nearestMetroStopRepository,
                                     OptionItemConverterForProperty optionItemConverter,
                                     MemberRepository memberRepository,
                                     WishItemRepository wishItemRepository
    ) {
        this.propertyConverterMap = converterList.stream()
                .collect(Collectors.toMap(PropertyConverter::supports, Function.identity()));
        this.nearestMetroStopRepository = nearestMetroStopRepository;
        this.optionItemConverter = optionItemConverter;
        this.memberRepository = memberRepository;
        this.wishItemRepository = wishItemRepository;
    }


    public <T> List<T> convertProperties(List<Property> properties, PropertyViewType viewType) {
        return convertPropertyListToDTO(properties, viewType);
    }

    public <T> T convertProperty(Property property, PropertyViewType viewType) {

        PropertyConverter<T> converter = getConverterByView(viewType);
        NearestMetroStop nearestMetroStop = getNearestMetroStop(property);
        Member host = property.getMember();

        MetaInfo metaInfo = buildMetaInfoForProperty(property, host);
        return converter.convert(buildPropertyConvertContext(property, nearestMetroStop, MemberSummaryDTO.from(host), metaInfo));
    }

    private MetaInfo buildMetaInfoForProperty(Property property, Member host) {
        MetaInfo metaInfo;
        Optional<Long> optRequesterId = SecurityContextUtils.getHttpRequesterId();
        if (optRequesterId.isEmpty()) {
            metaInfo = MetaInfo.create(false, false);
        } else {
            Long requesterId = optRequesterId.get();
            boolean isOwner = requesterId.equals(host.getId());
            boolean isWished = isWished(property, requesterId);

            metaInfo = MetaInfo.create(isOwner, isWished);
        }
        return metaInfo;
    }

    private boolean isWished(Property property, Long requesterId) {
        Member requester = memberRepository.findById(requesterId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        List<WishItem> propertyWishes = wishItemRepository.findByMemberAndTargetType(
                requester, WishTargetType.PROPERTY
        );

        List<Long> propertyIds = propertyWishes.stream()
                .map(WishItem::getTargetId)
                .toList();
        return propertyIds.contains(property.getId());
    }


    private <T> PropertyConverter<T> getConverterByView(PropertyViewType view) {
        PropertyConverter<?> converter = propertyConverterMap.getOrDefault(view, propertyConverterMap.get(PropertyViewType.DEFAULT));
        return (PropertyConverter<T>) converter;
    }

    private NearestMetroStop getNearestMetroStop(Property property) {
        return nearestMetroStopRepository.findByProperty(property)
                .orElseThrow(() -> new CustomException(ServiceCode.NEAREST_METRO_STOP_NOT_EXISTS));
    }

    private <T> List<T> convertPropertyListToDTO(
            List<Property> properties,
            PropertyViewType viewType
    ) {
        Optional<Long> optRequesterId = SecurityContextUtils.getHttpRequesterId();

        List<WishItem> wishItems = getWishItems(optRequesterId);

        Set<Long> wishedPropertyIds = wishItems.stream()
                .map(WishItem::getTargetId)
                .collect(Collectors.toSet());


        PropertyConverter<T> converter = getConverterByView(viewType);

        return properties.stream()
                .map(property -> {
                    NearestMetroStop nearestMetroStop = getNearestMetroStop(property);
                    Member host = property.getMember();

                    boolean isOwner = optRequesterId
                            .map(id -> id.equals(host.getId()))
                            .orElse(false);

                    boolean isWished = optRequesterId
                            .map(id -> wishedPropertyIds.contains(property.getId()))
                            .orElse(false);

                    MetaInfo metaInfo = new MetaInfo(isOwner, isWished);

                    PropertyConvertContext context = buildPropertyConvertContext(
                            property,
                            nearestMetroStop,
                            MemberSummaryDTO.from(host),
                            metaInfo
                    );
                    return converter.convert(context);
                })
                .collect(Collectors.toList());
    }

    private List<WishItem> getWishItems(Optional<Long> optRequesterId) {
        List<WishItem> wishItems;
        if (optRequesterId.isPresent()) {
            Long requesterId = optRequesterId.get();
            Member requester = memberRepository.findById(requesterId)
                    .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));
            wishItems = wishItemRepository.findByMemberAndTargetType(
                    requester, WishTargetType.PROPERTY);
            return wishItems;
        } else {
            return Collections.emptyList();
        }
    }

    private PropertyConvertContext buildPropertyConvertContext(Property property,
                                                               NearestMetroStop nearestMetroStop,
                                                               MemberSummaryDTO memberSummaryDTO,
                                                               MetaInfo metaInfo
    ) {
        return PropertyConvertContext.create(
                property,
                getOptionItemResponseDTOS(property),
                NearestMetroStopResponseDTO.from(nearestMetroStop),
                memberSummaryDTO,
                metaInfo
        );
    }

    private List<OptionItemResponseDTO> getOptionItemResponseDTOS(Property property) {
        List<OptionItemResponseDTO> optionItemsDTOs = optionItemConverter.toOptionItemResponseDTO(property.getOptionItems());
        return optionItemsDTOs;
    }
}