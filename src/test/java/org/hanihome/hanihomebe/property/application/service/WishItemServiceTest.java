package org.hanihome.hanihomebe.property.application.service;

import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.member.domain.Gender;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.domain.Role;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.ParkingOption;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.web.dto.SharePropertyCreateRequestDTO;
import org.hanihome.hanihomebe.wishlist.application.service.WishItemService;
import org.hanihome.hanihomebe.wishlist.domain.WishItem;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.hanihome.hanihomebe.wishlist.repository.WishItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class WishItemServiceTest {

    //주입
    @Autowired
    WishItemService wishItemService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    WishItemRepository wishItemRepository;

    @Autowired
    PropertyRepository propertyRepository;

    private Long memberId;
    private Long propertyId;

    @BeforeEach
    void init() {

        Member member = Member.builder()
                .email("verify@hanihome.com")
                .password("password")
                .birthDate(LocalDate.of(1999, 1, 1))
                .gender(Gender.FEMALE)
                .role(Role.GUEST)
                .name("테스트유저")
                .nickname("테스트닉")
                .phoneNumber("01012345678")
                .build();

        memberId = memberRepository.save(member).getId();

        SharePropertyCreateRequestDTO dto = new SharePropertyCreateRequestDTO(
                null,
                PropertySuperType.SHARE,
                GenderPreference.ANY,
                new Region("Australia", "2067", "NSW", "Chatswood", "Smith St", "25", "1203", "Chatswood Central Apartments"),  // Region은 Embedded 객체니까 생성자 맞춰줘야 해
                List.of("https://example.com/photo1.jpg", "https://example.com/photo2.jpg"),
                BigDecimal.valueOf(250),
                List.of(),  // optionItemIds
                "관리비 포함",
                BigDecimal.valueOf(500),
                BigDecimal.valueOf(200),
                2,
                4,
                "계약 조건 설명",
                Set.of(LocalDateTime.now().plusDays(3)),  // 입주 가능일
                ParkingOption.NONE,
                Set.of(LocalDateTime.now().plusDays(1)),  // 뷰잉 가능일
                "깔끔한 쉐어하우스입니다.",
                SharePropertySubType.SECOND_ROOM,
                15.5,
                100.0,
                4,
                2,
                3,
                2,
                CapacityShare.SINGLE
        );

        ShareProperty shareProperty = ShareProperty.create(dto, member);
        propertyId = propertyRepository.save(shareProperty).getId();

        /*
        WishItem wishItem = WishItem.createFrom(member, WishTargetType.PROPERTY, propertyId);
        wishItemRepository.save(wishItem);
         */

    }

    @Test
    @DisplayName("올바른 요청일 때 찜 추가")
    void addWishItemTest() {
        WishItem wishItem = wishItemService.addWishItem(memberId, WishTargetType.PROPERTY, propertyId);

        assertThat(wishItem).isNotNull();
        assertThat(wishItem.getId()).isNotNull();
        assertThat(wishItem.getTargetId()).isEqualTo(propertyId);
        assertThat(wishItem.getTargetType()).isEqualTo(WishTargetType.PROPERTY);
    }






}
