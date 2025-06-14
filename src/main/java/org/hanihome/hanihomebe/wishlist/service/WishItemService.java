package org.hanihome.hanihomebe.wishlist.service;

import jakarta.transaction.Transactional;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.wishlist.domain.WishItem;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.hanihome.hanihomebe.wishlist.repository.WishItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequestMapping
@Transactional
public class WishItemService {
    private final WishItemRepository wishItemRepository;
    private final PropertyRepository propertyRepository;
    private final MemberRepository memberRepository;

    public WishItemService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //매물 찜하기 Create
    public void addWishItemToProperty(Long memberId, WishTargetType targetType, Long targetId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));

        //프론트에서 type 잘못 넘겨줄 수 있으니 일단 if 추가
        if(targetType == WishTargetType.PROPERTY) {
            propertyRepository.findById(targetId)
                    .orElseThrow(() -> new CustomException(ServiceCode.))

        }
    }
}
