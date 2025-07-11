package org.hanihome.hanihomebe.property.application.service;

import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.member.domain.Gender;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.domain.Role;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.domain.*;
import org.hanihome.hanihomebe.property.domain.enums.CapacityShare;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.ParkingOption;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.enums.SharePropertySubType;
import org.hanihome.hanihomebe.property.domain.vo.*;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.web.dto.request.SharePropertyCreateRequestDTO;
import org.hanihome.hanihomebe.wishlist.application.service.WishItemService;
import org.hanihome.hanihomebe.wishlist.domain.WishItem;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.hanihome.hanihomebe.wishlist.repository.WishItemRepository;
import org.hanihome.hanihomebe.wishlist.web.dto.WishItemDTO;
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
import java.time.LocalTime;
import java.util.List;

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

        //given
        // 공통 객체 설정
        Region region = new Region(
                "Australia", "2067", "NSW", "Chatswood",
                "Smith St", "25", "1203",
                "Chatswood Central Apartments",
                BigDecimal.ZERO, BigDecimal.ZERO
        );
        List<String> photoUrls = List.of(
                "https://example.com/1.jpg",
                "https://example.com/2.jpg"
        );
        CostDetails costDetails = CostDetails.builder()
                .weeklyCost(new BigDecimal("100.00"))
                .deposit(new BigDecimal("800.00"))
                .keyDeposit(new BigDecimal("100.00"))
                .isBillIncluded(true)
                .costDescription("전기세, 수도세 포함")
                .build();
        LivingConditions livingConditions = new LivingConditions(
                4, 12, "6개월 이상 계약 가능", true
        );
        MoveInInfo moveInInfo = new MoveInInfo(
                LocalDateTime.of(2025,5,5,5,5),
                LocalDateTime.of(2025,5,10,5,5),
                true,
                false
        );
        List<TimeSlot> timeSlots = List.of(
                new TimeSlot(LocalTime.of(12,0), LocalTime.of(12,30)),
                new TimeSlot(LocalTime.of(15,0), LocalTime.of(15,30))
        );
        LocalDate meetingDateFrom = LocalDate.of(2025,5,10);
        LocalDate meetingDateTo   = LocalDate.of(2025,5,12);

        // Share Master Room 매물 생성
        SharePropertyCreateRequestDTO dto =
                new SharePropertyCreateRequestDTO(
                        memberId,
                        PropertySuperType.SHARE,
                        GenderPreference.ANY,
                        true,                     // lgbtAvailable
                        region,
                        photoUrls,
                        costDetails,
                        List.of(1L,2L,3L),        // optionItemIds
                        livingConditions,
                        moveInInfo,
                        ParkingOption.STREET_PARKING,
                        meetingDateFrom,
                        meetingDateTo,
                        timeSlots,
                        null,                     // viewingAvailableDateTimes
                        true,                     // viewingAlwaysAvailable
                        "깨끗하고 조용한 마스터룸입니다.",
                        SharePropertySubType.MASTER_ROOM,
                        new ShareInternalDetails(
                                10.0, 50.0,
                                3, 2, 5, 2
                        ),                      // internalDetails
                        CapacityShare.DOUBLE       // capacityShare
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
        WishItemDTO dto = wishItemService.addWishItem(memberId, WishTargetType.PROPERTY, propertyId);

        assertThat(dto).isNotNull();
        assertThat(dto.getTargetId()).isNotNull();
        assertThat(dto.getTargetId()).isEqualTo(propertyId);
        assertThat(dto.getTargetType()).isEqualTo(WishTargetType.PROPERTY);
    }







}
