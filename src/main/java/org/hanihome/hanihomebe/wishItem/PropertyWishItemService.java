package org.hanihome.hanihomebe.wishItem;


import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.application.service.PropertyConversionService;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.response.summary.PropertySummaryDTO;
import org.hanihome.hanihomebe.wishlist.domain.WishItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class PropertyWishItemService  {

    private final PropertyWishItemRepository propertyWishItemRepository;
    private final MemberRepository memberRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyConversionService propertyConversionService;

    public void addPropertyWishItem(Long propertyId, Long memberId) {
        Member member= memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new CustomException(ServiceCode.PROPERTY_NOT_EXISTS));

        if(isAlreadyWished(member,property)){
            throw new CustomException(ServiceCode.ALREADY_WISH_EXISTS);
        }

        PropertyWishItem propertyWishItem = PropertyWishItem.create(member,property);

        propertyWishItemRepository.save(propertyWishItem);

    }

    public List<PropertySummaryDTO> getPropertyWishItems(Long memberId, String sort) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        List<PropertyWishItem> propertyWishes = propertyWishItemRepository.findByMember(member); //이렇게 가져와서 직접 정렬하는게 빠를까? 아님 DB 조회할 떄 정렬된 쿼리 메서드를 날리는 게 효율적일까?

        //sort가 많아진다면 map으로 가야할 듯... 일단은 이렇게 가겠습니다.;

        List<Property> sortedProperties = switch (sort.toLowerCase()) {
            case "latest" -> propertyWishes.stream()
                    .sorted(Comparator.comparing(PropertyWishItem::getCreatedAt).reversed())
                    .map(PropertyWishItem::getProperty)
                    .toList();
            case "popular" -> propertyWishes.stream()
                    .map(PropertyWishItem::getProperty)
                    .sorted(Comparator.comparing(Property::getWishCount).reversed())
                    .toList();
            default -> throw new CustomException(ServiceCode.INVALID_SORT_OPTION);
        };

        return propertyConversionService.convertProperties(sortedProperties, PropertyViewType.SUMMARY);
    }

    public void deletePropertyWishItem(Long propertyId, Long memberId) {
        Member member= memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new CustomException(ServiceCode.PROPERTY_NOT_EXISTS));

        if(!isAlreadyWished(member,property)){
            throw new CustomException(ServiceCode.NOT_A_WISH);
        }

        propertyWishItemRepository.deleteByMemberAndProperty(member,property);

    }

    private boolean isAlreadyWished(Member member, Property property) {
        return propertyWishItemRepository.existsByMemberAndProperty(member, property);
    }
}
