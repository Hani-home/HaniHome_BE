package org.hanihome.hanihomebe.wishlist.application.service;

import jakarta.transaction.Transactional;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.application.PropertyMapper;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.hanihome.hanihomebe.wishlist.application.validation.WishTargetValidator;
import org.hanihome.hanihomebe.wishlist.domain.WishItem;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.hanihome.hanihomebe.wishlist.repository.WishItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequestMapping
@Transactional
public class WishItemService {
    private final WishItemRepository wishItemRepository;
    private final Map<WishTargetType, WishTargetValidator> validatorMap;
    private final MemberRepository memberRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final Map<WishTargetType, WishTargetService> wishTargetServiceMap;
    /*
    validatorMap = {
        PROPERTY → PropertyWishTargetValidator,
        ...
    }

     */

    @Autowired
    public WishItemService(MemberRepository memberRepository, WishItemRepository wishItemRepository, List<WishTargetValidator> validators, PropertyRepository propertyRepository, PropertyMapper propertyMapper, List<WishTargetService> targetServices) {
        this.memberRepository = memberRepository;
        this.wishItemRepository = wishItemRepository;
        this.validatorMap = validators.stream().collect(Collectors.toMap(WishTargetValidator::getType, v -> v));
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
        this.wishTargetServiceMap = targetServices.stream()
                .collect(Collectors.toMap(WishTargetService::getTargetType, s -> s));
    }

    //매물 찜하기 Create
    public void addWishItem(Long memberId, WishTargetType targetType, Long targetId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        //validator 꺼내기
        WishTargetValidator validator = validatorMap.get(targetType);
        if(validator == null) {
            throw new CustomException(ServiceCode.INVALID_TARGET_TYPE);
        }

        //검증 실행
        validator.validate(targetId);


        //이미 찜했는지 확인
        boolean alreadyExists = wishItemRepository.existsByMemberAndTargetTypeAndTargetId(
                member, targetType, targetId
        );
        if (alreadyExists) {
            throw new CustomException(ServiceCode.ALEADY_WISH_EXISTS);
        }

        WishItem wishItem = member.addWishItem(targetType, targetId);
        wishItemRepository.save(wishItem);
    }

    //삭제는 일단 추후. 영속성 떄문. property 단에서도 삭제를 해야하기 때문에 property 작업이 끝나면 진행하겠음

    /*
    부동산 찜 매물 조회, 다른 타입에 대해서 폭넓게 진행하고자 했는데 응답 DTO 맞추기가 어렵네요...
    근데 또 막상이러니 Property, PropertyRepository, propertyMapper 다 주입해서 더 별로인 거 같기도 하네요... 이것도 validate한거처럼 수정하는 게 더 나을지도..
     */

    public List<PropertyResponseDTO> getWishProperties(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        List<WishItem> propertyWishes = wishItemRepository.findByMemberAndTargetType(
                member, WishTargetType.PROPERTY
        );

        List<Long> propertyIds = propertyWishes.stream()
                .map(WishItem::getTargetId)
                .toList();

        List<Property> properties = propertyRepository.findAllById(propertyIds);

        return properties.stream()
                .map(propertyMapper::toResponseDto)
                .toList();
    }

    public Map<WishTargetType, List<?>> getAllWishItems(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        List<WishItem> allWishes = wishItemRepository.findByMember(member);

        // 타입별로 group by
        Map<WishTargetType, List<Long>> groupedIds = allWishes.stream()
                .collect(Collectors.groupingBy(
                        WishItem::getTargetType,
                        Collectors.mapping(WishItem::getTargetId, Collectors.toList())
                ));

        // 각 타입별 Service 사용해 DTO 변환
        Map<WishTargetType, List<?>> result = new HashMap<>();
        for (Map.Entry<WishTargetType, List<Long>> entry : groupedIds.entrySet()) {
            WishTargetService service = wishTargetServiceMap.get(entry.getKey());
            if (service != null) {
                result.put(entry.getKey(), service.getTargetDTOs(entry.getValue()));
            }
        }

        return result; // ex) { PROPERTY: [PropertyResponseDTO...], PRODUCT: [ProductResponseDTO...] }
    }
}
