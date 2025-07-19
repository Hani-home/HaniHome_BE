package org.hanihome.hanihomebe.property.application.service;

import org.hanihome.hanihomebe.deal.application.service.DealService;
import org.hanihome.hanihomebe.deal.domain.DealerType;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.hanihome.hanihomebe.member.domain.Gender;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.domain.Role;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.metro.domain.MetroStop;
import org.hanihome.hanihomebe.metro.repository.MetroStopRepository;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.vo.*;
import org.hanihome.hanihomebe.property.web.dto.request.PropertyCompleteTradeDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.RentPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.SharePropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.response.basic.PropertyResponseDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.viewing.application.service.ViewingService;
import org.hanihome.hanihomebe.viewing.domain.ViewingStatus;
import org.hanihome.hanihomebe.viewing.web.dto.ViewingCreateDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class PropertyServiceTest {

    @Autowired private PropertyService propertyService;
    @Autowired private ViewingService viewingService;
    @Autowired private DealService dealService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private OptionCategoryRepository optionCategoryRepository;
    @Autowired private OptionItemRepository optionItemRepository;
    @Autowired private MetroStopRepository metroStopRepository;

    private Long memberId;

    @BeforeAll
    void setUp() {
        createTestMetroStop();
        createTestOptionItems();
        memberId = createMember();
    }

    private void createTestMetroStop() {
        MetroStop parent = MetroStop.createParent(
                "200030", "Martin Place Station",
                new BigDecimal("37.5665"), new BigDecimal("126.9780"),
                "1", true, null
        );
        metroStopRepository.save(parent);
    }

    private void createTestOptionItems() {
        OptionCategory category = optionCategoryRepository.save(
                OptionCategory.create(CategoryCode.PROPERTY_CAT4)
        );
        optionItemRepository.save(OptionItem.createDefault(category, "item1"));
        optionItemRepository.save(OptionItem.createDefault(category, "item2"));
        optionItemRepository.save(OptionItem.createDefault(category, "item3"));
    }

    private Long createMember() {
        return memberRepository.save(
                Member.builder()
                        .email("test@hanihome.com")
                        .password("<PASSWORD>")
                        .birthDate(LocalDate.now())
                        .gender(Gender.MALE)
                        .role(Role.GUEST)
                        .phoneNumber("01012341234")
                        .name("olaf")
                        .nickname("olafNickname")
                        .build()
        ).getId();
    }
    @BeforeEach
    void setUpSecurityContext() {
        // ① 테스트 전용 사용자 디테일 생성
        CustomUserDetails user = new CustomUserDetails(memberId, "USER", "1234");
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // ② SecurityContext 에 Authentication 주입
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(token);
        SecurityContextHolder.setContext(ctx);
    }

    @Test
    @DisplayName("매물 생성 및 조회가 정상 동작한다")
    void createAndRetrieveProperty() {
        // Given
        SharePropertyCreateRequestDTO dto = buildSharePropertyDTO(memberId);

        // When
        var result = propertyService.createProperty(dto);

        // Then
        assertThat(propertyService.getPropertyById(result.id()).memberId())
                .isEqualTo(memberId);
    }

    @Test
    @DisplayName("뷰잉 예약 및 거래 완료 시나리오")
    void bookingAndDealFlow() {
        // Given: 호스트가 매물 등록
        SharePropertyCreateRequestDTO dto = buildSharePropertyDTO(memberId);
        PropertyResponseDTO created = propertyService.createProperty(dto);
        Long propertyId = created.id();

        // Given: Member2, Member3 생성
        Long member2Id = memberRepository.save(
                Member.builder()
                        .email("user2@hanihome.com").password("pwd2").birthDate(LocalDate.now())
                        .gender(Gender.MALE).role(Role.GUEST).phoneNumber("01000000002").name("user2").nickname("nick2").build()
        ).getId();
        Long member3Id = memberRepository.save(
                Member.builder()
                        .email("user3@hanihome.com").password("pwd3").birthDate(LocalDate.now())
                        .gender(Gender.FEMALE).role(Role.GUEST).phoneNumber("01000000003").name("user3").nickname("nick3").build()
        ).getId();

        // When: Member2,3 뷰잉 예약
        LocalDateTime time1 = LocalDateTime.of(2025,8,10,9,0);
        LocalDateTime time2 = LocalDateTime.of(2025,8,12,15,0);
        Long viewingId2 = viewingService.createViewing(
                new ViewingCreateDTO(
                        propertyId,
                        new ArrayList<>(List.of(time1))
                ),
                member2Id
        ).getId();
        Long viewingId3 = viewingService.createViewing(
                new ViewingCreateDTO(
                        propertyId,
                        new ArrayList<>(List.of(time2))
                ),
                member3Id
        ).getId();

        // When: Member2 거래 완료
        propertyService.completeTrade(
                PropertyCompleteTradeDTO.create(memberId, viewingId2, propertyId)
        );

        // Then: 거래가 생성되고, property 상태 변경
        OptionItem cancelReason = optionItemRepository.findByItemName("이미 계약이 완료됐어요")
                .stream().findFirst().orElseThrow();

        assertThat(dealService.getDealsByDealerType(member2Id, DealerType.DEAL_AS_GUEST)).isNotEmpty();
        assertThat(propertyService.getPropertyById(propertyId).tradeStatus())
                .isEqualTo(TradeStatus.COMPLETED);
        assertThat(viewingService.getViewingById(viewingId3).getStatus())
                .isEqualTo(ViewingStatus.CANCELLED);
        assertThat(
                viewingService.getViewingById(viewingId3).getOptionItems()
                        .stream()
                        .anyMatch(optionItem -> optionItem.optionItemId().equals(cancelReason.getId()))
        );

        // Then: Member2는 거래 내역이 1 건, Member3은 0 건
        assertThat(dealService.getDealsByDealerType(member2Id, DealerType.DEAL_AS_GUEST)).hasSize(1);
        assertThat(dealService.getDealsByDealerType(member3Id, DealerType.DEAL_AS_GUEST)).isEmpty();
    }

    @Test
    @DisplayName("뷰잉 예약 및 거래 완료 시나리오")
    void bookingAndDealFlow_RentProperty() {
        // Given: 호스트가 매물 등록
        RentPropertyCreateRequestDTO dto = buildRentPropertyDTO(memberId);
        PropertyResponseDTO created = propertyService.createProperty(dto);
        Long propertyId = created.id();

        // Given: Member2, Member3 생성
        Long member2Id = memberRepository.save(
                Member.builder()
                        .email("user2@hanihome.com").password("pwd2").birthDate(LocalDate.now())
                        .gender(Gender.MALE).role(Role.GUEST).phoneNumber("01000000002").name("user2").nickname("nick2").build()
        ).getId();
        Long member3Id = memberRepository.save(
                Member.builder()
                        .email("user3@hanihome.com").password("pwd3").birthDate(LocalDate.now())
                        .gender(Gender.FEMALE).role(Role.GUEST).phoneNumber("01000000003").name("user3").nickname("nick3").build()
        ).getId();

        // When: Member2,3 뷰잉 예약
        LocalDateTime time1 = LocalDateTime.of(2025,8,10,9,0);
        LocalDateTime time2 = LocalDateTime.of(2025,8,12,15,0);
        Long viewingId2 = viewingService.createViewing(
                new ViewingCreateDTO(
                        propertyId,
                        new ArrayList<>(List.of(time1))
                ),
                member2Id
        ).getId();
        Long viewingId3 = viewingService.createViewing(
                new ViewingCreateDTO(
                        propertyId,
                        new ArrayList<>(List.of(time2))
                ),
                member3Id
        ).getId();

        // When: Member2 거래 완료
        propertyService.completeTrade(
                PropertyCompleteTradeDTO.create(memberId, viewingId2, propertyId)
        );

        // Then: 거래가 생성되고, property 상태 변경
        OptionItem cancelReason = optionItemRepository.findByItemName("이미 계약이 완료됐어요")
                .stream().findFirst().orElseThrow();

        assertThat(dealService.getDealsByDealerType(member2Id, DealerType.DEAL_AS_GUEST)).isNotEmpty();
        assertThat(propertyService.getPropertyById(propertyId).tradeStatus())
                .isEqualTo(TradeStatus.COMPLETED);
        assertThat(viewingService.getViewingById(viewingId3).getStatus())
                .isEqualTo(ViewingStatus.CANCELLED);
        assertThat(
                viewingService.getViewingById(viewingId3).getOptionItems()
                        .stream()
                        .anyMatch(optionItem -> optionItem.optionItemId().equals(cancelReason.getId()))
        );

        // Then: Member2는 거래 내역이 1 건, Member3은 0 건
        assertThat(dealService.getDealsByDealerType(member2Id, DealerType.DEAL_AS_GUEST)).hasSize(1);
        assertThat(dealService.getDealsByDealerType(member3Id, DealerType.DEAL_AS_GUEST)).isEmpty();
    }

    // Helper to build DTO
    private SharePropertyCreateRequestDTO buildSharePropertyDTO(Long memberId) {
        Region region = new Region(
                "Australia", "2067", "NSW", "Chatswood",
                "Smith St", "25", "1203",
                "Chatswood Central Apartments",
                BigDecimal.ZERO, BigDecimal.ZERO
        );
        List<String> photoUrls = new ArrayList<>(List.of(
                "https://ex.com/1.jpg",
                "https://ex.com/2.jpg"
        ));
        CostDetails costDetails = CostDetails.builder()
                .weeklyCost(new BigDecimal("100"))
                .deposit(new BigDecimal("500"))
                .keyDeposit(new BigDecimal("50"))
                .isBillIncluded(true)
                .costDescription("All-included")
                .build();
        List<Long> optionItemIds = new ArrayList<>(List.of(1L, 2L, 3L));
        LivingConditions livingConditions = new LivingConditions(4, 12, "계약조건", true);
        MoveInInfo moveInInfo = new MoveInInfo(
                LocalDateTime.of(2025,8,1,9,0),
                LocalDateTime.of(2025,8,5,9,0), true, false
        );
        List<TimeSlot> timeSlots = new ArrayList<>(List.of(
                new TimeSlot(LocalTime.of(9,0), LocalTime.of(9,30)),
                new TimeSlot(LocalTime.of(15,0), LocalTime.of(15,30))
        ));
        return new SharePropertyCreateRequestDTO(
                memberId,
                PropertySuperType.SHARE,
                GenderPreference.ANY,
                true,
                region,
                photoUrls,
                costDetails,
                optionItemIds,
                livingConditions,
                moveInInfo,
                LocalDate.of(2025,8,10),
                LocalDate.of(2025,8,12),
                timeSlots,
                null,
                true,
                "설명",
                SharePropertySubType.MASTER_ROOM,
                new ShareInternalDetails(5.0, 10.0, 1, 2, 3, 4),
                CapacityShare.DOUBLE
        );
    }
    private RentPropertyCreateRequestDTO buildRentPropertyDTO(Long memberId) {
        Region region = new Region(
                "Australia", "2067", "NSW", "Chatswood",
                "Smith St", "25", "1203",
                "Chatswood Central Apartments",
                BigDecimal.ZERO, BigDecimal.ZERO
        );
        List<String> photoUrls = new ArrayList<>(List.of(
                "https://ex.com/1.jpg",
                "https://ex.com/2.jpg"
        ));
        CostDetails costDetails = CostDetails.builder()
                .weeklyCost(new BigDecimal("100"))
                .deposit(new BigDecimal("500"))
                .keyDeposit(new BigDecimal("50"))
                .isBillIncluded(true)
                .costDescription("All-included")
                .build();
        List<Long> optionItemIds = new ArrayList<>(List.of(1L, 2L, 3L));
        LivingConditions livingConditions = new LivingConditions(4, 12, "계약조건", true);
        MoveInInfo moveInInfo = new MoveInInfo(
                LocalDateTime.of(2025,8,1,9,0),
                LocalDateTime.of(2025,8,5,9,0), true, false
        );
        List<TimeSlot> timeSlots = new ArrayList<>(List.of(
                new TimeSlot(LocalTime.of(9,0), LocalTime.of(9,30)),
                new TimeSlot(LocalTime.of(15,0), LocalTime.of(15,30))
        ));
        return new RentPropertyCreateRequestDTO(
                memberId,
                PropertySuperType.RENT,
                GenderPreference.ANY,
                true,
                region,
                photoUrls,
                costDetails,
                optionItemIds,
                livingConditions,
                moveInInfo,
                LocalDate.of(2025, 8, 10),
                LocalDate.of(2025, 8, 12),
                timeSlots,
                null,
                true,
                "설명",
                RentPropertySubType.UNIT,
                new RentInternalDetails(5.0, 10.0, 1, 2, 3, 4),
                CapacityRent.FOUR
        );
    }
/*    private SharePropertyCreateRequestDTO buildSharePropertyDTO() {
        Region region = new Region(
                "Australia", "2067", "NSW", "Chatswood",
                "Smith St", "25", "1203",
                "Chatswood Central Apartments",
                BigDecimal.ZERO, BigDecimal.ZERO
        );
        List<String> photoUrls = new ArrayList<>(List.of(
                "https://example.com/1.jpg",
                "https://example.com/2.jpg"
        ));
        CostDetails costDetails = CostDetails.builder()
                .weeklyCost(new BigDecimal("100.00"))
                .deposit(new BigDecimal("800.00"))
                .keyDeposit(new BigDecimal("100.00"))
                .isBillIncluded(true)
                .costDescription("전기세, 수도세 포함")
                .build();
        LivingConditions living = new LivingConditions(4, 12, "6개월 이상 계약 가능", true);
        MoveInInfo moveIn = new MoveInInfo(
                LocalDateTime.of(2025,5,5,5,5),
                LocalDateTime.of(2025,5,10,5,5), true, false
        );
        List<TimeSlot> slots = new ArrayList<>(List.of(
                new TimeSlot(LocalTime.of(12,0), LocalTime.of(12,30)),
                new TimeSlot(LocalTime.of(15,0), LocalTime.of(15,30))
        ));
        SharePropertyCreateRequestDTO dto = new SharePropertyCreateRequestDTO(
                memberId,
                PropertySuperType.SHARE,
                GenderPreference.ANY,
                true,
                region,
                photoUrls,
                costDetails,
                new ArrayList<>(List.of(1L, 2L, 3L)),
                living,
                moveIn,
                LocalDate.of(2025,5,10),
                LocalDate.of(2025,5,12),
                slots,
                null,
                true,
                "깨끗하고 조용한 마스터룸입니다.",
                SharePropertySubType.MASTER_ROOM,
                new ShareInternalDetails(10.0, 50.0, 3, 2, 5, 2),
                CapacityShare.DOUBLE
        );
        return dto;
    }*/
}

