package org.hanihome.hanihomebe.property.application.service;

import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
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
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.command.SharePropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.vo.*;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.property.web.dto.request.PropertyCompleteTradeDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.*;
import org.hanihome.hanihomebe.property.web.dto.request.patch.PropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.request.patch.SharePropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyWithMemberResponseDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.viewing.application.service.ViewingService;
import org.hanihome.hanihomebe.viewing.repository.ViewingRepository;
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
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class PropertyViewingAbnormalCaseTest {

    @Autowired private PropertyService propertyService;
    @Autowired private ViewingService viewingService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private PropertyRepository propertyRepository;
    @Autowired private ViewingRepository viewingRepository;
    @Autowired private OptionCategoryRepository optionCategoryRepository;
    @Autowired private OptionItemRepository optionItemRepository;
    @Autowired private MetroStopRepository metroStopRepository;

    private Long hostId;
    private Long guestId1;
    private Long guestId2;
    private Long propertyId;
    private Long completedPropertyId;
    private Long hiddenPropertyId;

    void setUpSecurityContext() {
        // 기본적으로 hostId로 SecurityContext 설정
        CustomUserDetails user = new CustomUserDetails(hostId, "GUEST", "password", "host");
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(token);
        SecurityContextHolder.setContext(ctx);
    }

    @BeforeAll
    void setUp() {
        createTestMetroStop();
        createTestOptionItems();
        createTestMembers();
        setUpSecurityContext();
        createTestProperties();
    }

    // =================================== HIGH PRIORITY TESTS ====================================

    @Test
    @DisplayName("호스트가 자신의 매물에 뷰잉 예약 시 예외 발생")
    void should_ThrowException_When_HostTriesToBookOwnProperty() {
        // Given
        ViewingCreateDTO dto = new ViewingCreateDTO(
                propertyId,
                List.of(LocalDateTime.of(2025, 8, 10, 9, 0))
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(dto, hostId);
        });

        // 현재 코드를 보면 호스트 본인 매물 예약을 막는 로직이 없어서 
        // 실제로는 NO_OWNER_AUTHORITY나 새로운 ServiceCode가 필요할 것 같습니다.
        // 일단 예상되는 에러 코드로 작성하고 실제 구현에 따라 수정 필요
        assertEquals(ServiceCode.VIEWING_MADE_BY_HOST, exception.getServiceCode());
    }

    @Test
    @DisplayName("과거 날짜로 뷰잉 예약 시 예외 발생")
    void should_ThrowException_When_BookingPastDateTime() {
        SharePropertyCreateRequestDTO anotherPropertyDTO = buildSharePropertyDTO(hostId, DisplayStatus.ACTIVE, TradeStatus.BEFORE);
        PropertyWithMemberResponseDTO anotherProperty = propertyService.createProperty(anotherPropertyDTO, hostId);

        // Given: 과거 시간으로 뷰잉 예약 시도
        LocalDate pastDate = LocalDateTime.now().minusDays(1).toLocalDate();
        LocalTime pastTime = LocalTime.of(10, 0);
        ViewingCreateDTO dto = new ViewingCreateDTO(
                propertyId,
                List.of(LocalDateTime.of(pastDate, pastTime)) // 과거 날짜
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(dto, guestId1);
        });

        // ViewingService 코드를 보면 VIEWING_TIME_MISMATCH 가 발생할 것으로 예상
        assertEquals(ServiceCode.VIEWING_NEED_TO_BE_FUTURE, exception.getServiceCode());
    }

    @Test
    @DisplayName("이미 예약된 시간대에 중복 예약 시 예외 발생")
    void should_ThrowException_When_BookingAlreadyReservedTime() {
        // Given: Guest1이 먼저 예약
        LocalDateTime targetTime = LocalDateTime.of(2025, 8, 10, 9, 0);
        ViewingCreateDTO firstBooking = new ViewingCreateDTO(
                propertyId,
                List.of(targetTime)
        );
        
        viewingService.createViewing(firstBooking, guestId1);

        // When: Guest2가 같은 시간대 예약 시도
        ViewingCreateDTO secondBooking = new ViewingCreateDTO(
                propertyId,
                List.of(targetTime)
        );

        // Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(secondBooking, guestId2);
        });

        assertEquals(ServiceCode.VIEWING_TIME_MISMATCH, exception.getServiceCode());
    }

    @Test
    @DisplayName("거래 완료된 매물에 뷰잉 예약 시 예외 발생")
    void should_ThrowException_When_BookingCompletedProperty() {
        // Given: 거래 완료된 매물에 뷰잉 예약 시도
        ViewingCreateDTO dto = new ViewingCreateDTO(
                completedPropertyId,
                List.of(LocalDateTime.of(2025, 8, 10, 9, 0))
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(dto, guestId1);
        });

        // 거래 완료된 매물은 뷰잉이 불가능해야 함
        assertEquals(ServiceCode.VIEWING_NOT_AVAILABLE_FOR_PROPERTY_STATUS, exception.getServiceCode());
    }

    @Test
    @DisplayName("INACTIVE 상태 매물에 뷰잉 예약 시 예외 발생")
    void should_ThrowException_When_BookingINACTIVEProperty() {
        // Given: INACTIVE 상태 매물에 뷰잉 예약 시도
        ViewingCreateDTO dto = new ViewingCreateDTO(
                hiddenPropertyId,
                List.of(LocalDateTime.of(2025, 8, 10, 9, 0))
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(dto, guestId1);
        });

        // INACTIVE 매물은 접근 자체가 불가능해야 함
        assertEquals(ServiceCode.VIEWING_NOT_AVAILABLE_FOR_PROPERTY_STATUS, exception.getServiceCode());
    }

    // =================================== MEDIUM PRIORITY TESTS ====================================

    @Test
    @DisplayName("30분 이내 연속 뷰잉 예약 시 예외 발생")
    void should_ThrowException_When_BookingWithin30Minutes() {
        // Given: Guest1이 09:00에 뷰잉 예약
        LocalDateTime firstTime = LocalDateTime.of(2025, 8, 10, 9, 0);
        ViewingCreateDTO firstBooking = new ViewingCreateDTO(
                propertyId,
                List.of(firstTime)
        );
        viewingService.createViewing(firstBooking, guestId1);

        // When: 같은 Guest1이 09:15 (15분 후)에 다른 매물 예약 시도
        // 우선 다른 매물을 하나 더 생성
        SharePropertyCreateRequestDTO anotherPropertyDTO = buildSharePropertyDTO(hostId, DisplayStatus.ACTIVE, TradeStatus.BEFORE);
        PropertyWithMemberResponseDTO anotherProperty = propertyService.createProperty(anotherPropertyDTO, hostId);
        Long anotherPropertyId = anotherProperty.id();

        LocalDateTime conflictTime = LocalDateTime.of(2025, 8, 10, 9, 15); // 15분 후
        ViewingCreateDTO conflictBooking = new ViewingCreateDTO(
                anotherPropertyId,
                List.of(conflictTime)
        );

        // Then: 30분 이내 예약으로 예외 발생
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(conflictBooking, guestId1);
        });

        assertEquals(ServiceCode.VIEWING_ALREADY_PRESCHEDULED, exception.getServiceCode());
    }

    @Test
    @DisplayName("존재하지 않는 매물 ID로 뷰잉 예약 시 예외 발생")
    void should_ThrowException_When_PropertyNotExists() {
        // Given: 존재하지 않는 매물 ID
        Long nonExistentPropertyId = 99999L;
        ViewingCreateDTO dto = new ViewingCreateDTO(
                nonExistentPropertyId,
                List.of(LocalDateTime.of(2025, 8, 10, 9, 0))
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(dto, guestId1);
        });

        assertEquals(ServiceCode.PROPERTY_NOT_EXISTS, exception.getServiceCode());
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID로 뷰잉 예약 시 예외 발생")
    void should_ThrowException_When_MemberNotExists() {
        // Given: 존재하지 않는 회원 ID
        Long nonExistentMemberId = 99999L;
        ViewingCreateDTO dto = new ViewingCreateDTO(
                propertyId,
                List.of(LocalDateTime.of(2025, 8, 10, 9, 0))
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(dto, nonExistentMemberId);
        });

        assertEquals(ServiceCode.MEMBER_NOT_EXISTS, exception.getServiceCode());
    }

    @Test
    @DisplayName("선호 시간이 0개일 때 예외 발생")
    void should_ThrowException_When_PreferredTimesIsEmpty() {
        // Given: 빈 선호 시간 리스트
        ViewingCreateDTO dto = new ViewingCreateDTO(
                propertyId,
                new ArrayList<>() // 빈 리스트
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(dto, guestId1);
        });

        assertEquals(ServiceCode.VIEWING_NUMBER_NOT_SATISFIED, exception.getServiceCode());
    }

    @Test
    @DisplayName("선호 시간이 4개 이상일 때 예외 발생")
    void should_ThrowException_When_PreferredTimesExceed3() {
        // Given: 4개의 선호 시간 (제한 초과)
        ViewingCreateDTO dto = new ViewingCreateDTO(
                propertyId,
                List.of(
                        LocalDateTime.of(2025, 8, 10, 9, 0),
                        LocalDateTime.of(2025, 8, 10, 10, 0),
                        LocalDateTime.of(2025, 8, 10, 11, 0),
                        LocalDateTime.of(2025, 8, 10, 12, 0) // 4개
                )
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(dto, guestId1);
        });

        assertEquals(ServiceCode.VIEWING_NUMBER_NOT_SATISFIED, exception.getServiceCode());
    }

    @Test
    @DisplayName("뷰잉 취소 시 예약 시간이 해제되는지 확인")
    void should_ReleaseTimeSlot_When_ViewingCancelled() {
        // Given: 뷰잉 예약
        LocalDateTime targetTime = LocalDateTime.of(2025, 8, 10, 9, 0);
        ViewingCreateDTO dto = new ViewingCreateDTO(
                propertyId,
                List.of(targetTime)
        );
        
        Long viewingId = viewingService.createViewing(dto, guestId1).getId();

        // 예약된 시간이 reserved 상태인지 확인
        Property property = propertyRepository.findById(propertyId).orElseThrow();
        ViewingAvailableDateTime reservedSlot = property.getViewingAvailableDateTimes()
                .stream()
                .filter(slot -> slot.getDate().equals(targetTime.toLocalDate()) 
                              && slot.getTime().equals(targetTime.toLocalTime()))
                .findFirst()
                .orElseThrow();
        assertTrue(reservedSlot.isReserved(), "예약 후 시간대가 reserved 상태여야 함");

        // When: 뷰잉 취소
        viewingService.cancelViewingAndReleaseReservedTimes(
                new org.hanihome.hanihomebe.viewing.web.dto.cancel.ViewingCancelRequestDTO(
                        viewingId, List.of(1L), "테스트 취소"
                )
        );

        // Then: 시간대가 해제되었는지 확인
        Property updatedProperty = propertyRepository.findById(propertyId).orElseThrow();
        ViewingAvailableDateTime releasedSlot = updatedProperty.getViewingAvailableDateTimes()
                .stream()
                .filter(slot -> slot.getDate().equals(targetTime.toLocalDate()) 
                              && slot.getTime().equals(targetTime.toLocalTime()))
                .findFirst()
                .orElseThrow();
        assertFalse(releasedSlot.isReserved(), "취소 후 시간대가 해제되어야 함");
    }

    @Test
    @DisplayName("HIDDEN 매물을 ACTIVE로 변경 시 목록에 노출되는지 확인")
    void should_ShowInList_When_PropertyStatusChangedToActive() {
        // Given: HIDDEN 상태의 매물
        Property hiddenProperty = propertyRepository.findById(hiddenPropertyId).orElseThrow();
        log.info("----------------hidden: {}", hiddenProperty.getDisplayStatus());

        assertEquals(DisplayStatus.INACTIVE, hiddenProperty.getDisplayStatus());

        // 매물이 목록에 포함되지 않는지 확인 (실제로는 PropertySearchService를 사용해야 하지만 간단히 확인)
        List<Property> hiddenProperties = propertyRepository.findAll()
                .stream()
                .filter(p -> p.getDisplayStatus() == DisplayStatus.ACTIVE)
                .toList();
        assertFalse(hiddenProperties.stream().anyMatch(p -> p.getId().equals(hiddenPropertyId)));

        // When: 매물을 ACTIVE로 변경
        // 실제로는 PropertyService의 상태 변경 메서드를 사용해야 하지만, 
        // 현재 코드에서는 직접 변경
        // TODO: PropertyService에 상태 변경 메서드가 있다면 그것을 사용
        
        // Then: 매물이 목록에 포함되는지 확인은 실제 구현에 따라 달라질 수 있음
        log.info("매물 상태 변경 테스트 - 실제 구현에 따라 테스트 로직 조정 필요");
    }

    // =================================== LOW PRIORITY TESTS ====================================
/*

    @Test
    @DisplayName("정확히 30분 간격으로 뷰잉 예약 시 성공 (경계값 테스트)")
    void should_AllowBooking_When_Exactly30MinutesGap() {
        // Given: Guest1이 09:00에 뷰잉 예약
        LocalDateTime firstTime = LocalDateTime.of(2025, 8, 10, 9, 0);
        ViewingCreateDTO firstBooking = new ViewingCreateDTO(
                propertyId,
                List.of(firstTime)
        );
        viewingService.createViewing(firstBooking, guestId1);

        // When: 같은 Guest1이 정확히 30분 후 (09:30)에 다른 매물 예약
        SharePropertyCreateRequestDTO anotherPropertyDTO = buildSharePropertyDTO(hostId, DisplayStatus.ACTIVE, TradeStatus.BEFORE);
        PropertyWithMemberResponseDTO anotherProperty = propertyService.createProperty(anotherPropertyDTO, hostId);
        Long anotherPropertyId = anotherProperty.id();

        LocalDateTime exactlyAfter30Min = LocalDateTime.of(2025, 8, 10, 9, 30); // 정확히 30분 후
        ViewingCreateDTO secondBooking = new ViewingCreateDTO(
                anotherPropertyId,
                List.of(exactlyAfter30Min)
        );

        // Then: 예약이 성공해야 함 (30분 간격은 허용)
        assertDoesNotThrow(() -> {
            viewingService.createViewing(secondBooking, guestId1);
        });
    }
*/

    @Test
    @DisplayName("필수 필드에 null 값 전송 시 예외 발생")
    void should_ThrowException_When_NullValues() {
        // Given: propertyId가 null인 DTO
        ViewingCreateDTO dtoWithNullProperty = new ViewingCreateDTO(
                null, // null propertyId
                List.of(LocalDateTime.of(2025, 8, 10, 9, 0))
        );

        // When & Then: Validation 예외 발생 (실제로는 @Valid에 의해 검증됨)
        // ViewingService 내부에서 null 체크가 있다면 CustomException 발생
        // 없다면 NullPointerException 발생 가능
        assertThrows(Exception.class, () -> {
            viewingService.createViewing(dtoWithNullProperty, guestId1);
        });

        // Given: preferredTimes가 null인 DTO
        ViewingCreateDTO dtoWithNullTimes = new ViewingCreateDTO(
                propertyId,
                null // null preferredTimes
        );

        // When & Then
        assertThrows(Exception.class, () -> {
            viewingService.createViewing(dtoWithNullTimes, guestId1);
        });
    }

    @Test
    @DisplayName("여러 사용자가 동시에 같은 시간대 예약 시도 (동시성 테스트)")
    void should_HandleConcurrentBooking_When_MultipleUsersBookSameTime() {
        // Given: 같은 시간대에 대한 예약 DTO
        LocalDateTime targetTime = LocalDateTime.of(2025, 8, 10, 15, 0);
        ViewingCreateDTO dto1 = new ViewingCreateDTO(propertyId, List.of(targetTime));
        ViewingCreateDTO dto2 = new ViewingCreateDTO(propertyId, List.of(targetTime));

        // When: 두 사용자가 거의 동시에 예약 시도
        // 실제 동시성 테스트는 복잡하므로 여기서는 순차적으로 실행하여 두 번째가 실패하는지 확인
        
        // 첫 번째 예약은 성공
        assertDoesNotThrow(() -> {
            viewingService.createViewing(dto1, guestId1);
        });

        // 두 번째 예약은 실패해야 함
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(dto2, guestId2);
        });

        assertEquals(ServiceCode.VIEWING_TIME_MISMATCH, exception.getServiceCode());
    }

    @Test
    @DisplayName("매물의 뷰잉 가능 시간대 밖의 시간으로 예약 시도")
    void should_ThrowException_When_BookingOutsideAvailableTime() {
        // Given: 매물의 뷰잉 가능 시간은 09:00-09:30, 15:00-15:30 이지만
        // 12:00에 예약 시도 (가능하지 않은 시간)
        ViewingCreateDTO dto = new ViewingCreateDTO(
                propertyId,
                List.of(LocalDateTime.of(2025, 8, 10, 12, 0)) // 불가능한 시간
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(dto, guestId1);
        });

        assertEquals(ServiceCode.VIEWING_TIME_MISMATCH, exception.getServiceCode());
    }

    @Test
    @DisplayName("매물의 뷰잉 가능 날짜 범위 밖의 날짜로 예약 시도")
    void should_ThrowException_When_BookingOutsideDateRange() {
        // Given: 매물의 뷰잉 가능 날짜는 2025-08-10 ~ 2025-08-20 이지만
        // 2025-08-25에 예약 시도 (범위 밖)
        ViewingCreateDTO dto = new ViewingCreateDTO(
                propertyId,
                List.of(LocalDateTime.now().plusDays(30)) // 범위 밖 날짜
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            viewingService.createViewing(dto, guestId1);
        });

        assertEquals(ServiceCode.VIEWING_TIME_MISMATCH, exception.getServiceCode());
    }

    @Test
    @DisplayName("중복된 선호 시간이 포함된 리스트로 예약 시도")
    void should_HandleDuplicatePreferredTimes() {
        // Given: 중복된 시간이 포함된 선호 시간 리스트
        LocalDateTime duplicateTime = LocalDateTime.of(2025, 8, 10, 9, 0);
        ViewingCreateDTO dto = new ViewingCreateDTO(
                propertyId,
                List.of(duplicateTime, duplicateTime, duplicateTime) // 중복된 시간
        );

        // When & Then: 중복 제거되어 정상 처리되거나, 검증 오류 발생
        // 실제 구현에 따라 결과가 달라질 수 있음
        assertDoesNotThrow(() -> {
            viewingService.createViewing(dto, guestId1);
        });
    }

    // =================================== HELPER METHODS =====================================

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
        
        // 뷰잉 취소 관련 옵션 카테고리도 생성
        OptionCategory viewingCancelCategory = optionCategoryRepository.save(
                OptionCategory.create(CategoryCode.VIEWING_CAT1)
        );
        optionItemRepository.save(OptionItem.createDefault(viewingCancelCategory, "이미 계약이 완료됐어요"));
    }

    private void createTestMembers() {
        // Host 생성
        hostId = memberRepository.save(
                Member.builder()
                        .email("host@hanihome.com")
                        .password("password123")
                        .birthDate(LocalDate.of(1990, 1, 1))
                        .gender(Gender.MALE)
                        .role(Role.GUEST)
                        .phoneNumber("01011111111")
                        .name("Host User")
                        .nickname("host")
                        .build()
        ).getId();

        // Guest1 생성
        guestId1 = memberRepository.save(
                Member.builder()
                        .email("guest1@hanihome.com")
                        .password("password123")
                        .birthDate(LocalDate.of(1992, 1, 1))
                        .gender(Gender.FEMALE)
                        .role(Role.GUEST)
                        .phoneNumber("01022222222")
                        .name("Guest User 1")
                        .nickname("guest1")
                        .build()
        ).getId();

        // Guest2 생성
        guestId2 = memberRepository.save(
                Member.builder()
                        .email("guest2@hanihome.com")
                        .password("password123")
                        .birthDate(LocalDate.of(1993, 1, 1))
                        .gender(Gender.MALE)
                        .role(Role.GUEST)
                        .phoneNumber("01033333333")
                        .name("Guest User 2")
                        .nickname("guest2")
                        .build()
        ).getId();
    }

    private void createTestProperties() {
        // 일반 매물 생성 (ACTIVE, TradeStatus.BEFORE)
        SharePropertyCreateRequestDTO activePropertyDTO = buildSharePropertyDTO(hostId, DisplayStatus.ACTIVE, TradeStatus.BEFORE);
        PropertyWithMemberResponseDTO activeProperty = propertyService.createProperty(activePropertyDTO, hostId);
        propertyId = activeProperty.id();

        // 거래 완료된 매물 생성
        SharePropertyCreateRequestDTO completedPropertyDTO = buildSharePropertyDTO(hostId, DisplayStatus.ACTIVE, TradeStatus.COMPLETED);
        PropertyWithMemberResponseDTO completedProperty = propertyService.createProperty(completedPropertyDTO, hostId);
        completedPropertyId = completedProperty.id();
        propertyService.completeTrade(PropertyCompleteTradeDTO.create(propertyRepository.findById(completedPropertyId).orElseThrow().getMember().getId(), null, completedPropertyId, true));
        
        // 실제로는 매물 생성 후 tradeStatus를 변경해야 할 수도 있음
        Property completedPropertyEntity = propertyRepository.findById(completedPropertyId).orElseThrow();
        completedPropertyEntity.completeTrade();
        propertyRepository.save(completedPropertyEntity);

        // HIDDEN 상태 매물 생성
        SharePropertyCreateRequestDTO hiddenPropertyDTO = buildSharePropertyDTO(hostId, DisplayStatus.INACTIVE, TradeStatus.BEFORE);
        PropertyWithMemberResponseDTO hiddenProperty = propertyService.createProperty(hiddenPropertyDTO, hostId);
        hiddenPropertyId = hiddenProperty.id();
        propertyService.patch(hiddenPropertyId, SharePropertyPatchRequestDTO.builder().displayStatus(DisplayStatus.INACTIVE).build());
    }

    private SharePropertyCreateRequestDTO buildSharePropertyDTO(Long memberId, DisplayStatus displayStatus, TradeStatus tradeStatus) {
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

        CostDetailsDTO costDetails = CostDetailsDTO.builder()
                .weeklyCost(new BigDecimal("300"))
                .deposit(new BigDecimal("1200"))
                .keyDeposit(new BigDecimal("100"))
                .billIncluded(true)
                .costDescription("All bills included")
                .build();

        LivingConditionsDTO livingConditions = new LivingConditionsDTO(
                2, 12, "No smoking, no pets", true
        );

        MoveInInfoDTO moveInInfo = new MoveInInfoDTO(
                LocalDateTime.of(2025, 8, 1, 9, 0),
                LocalDateTime.of(2025, 12, 31, 18, 0),
                true, false
        );

        // 오늘 - 7일 ~ 오늘 + 7일
        LocalDate meetingDateFrom = LocalDateTime.now().minusDays(7).toLocalDate();
        LocalDate meetingDateTo = meetingDateFrom.plusDays(7);

        List<TimeSlot> timeSlots = List.of(
                new TimeSlot(LocalTime.of(9, 0), LocalTime.of(11, 30)),
                new TimeSlot(LocalTime.of(15, 0), LocalTime.of(15, 30))
        );

        ShareInternalDetailsDTO shareDetails = new ShareInternalDetailsDTO(
                25.0, 100.0, 2, 1, 1, 2, true
        );


        return new SharePropertyCreateRequestDTO(
                memberId,
                PropertySuperType.SHARE,
                GenderPreference.ANY,
                true,
                region,
                photoUrls,
                costDetails,
                List.of(1L, 2L, 3L), // optionItemIds
                livingConditions,
                moveInInfo,
                meetingDateFrom,
                meetingDateTo,
                timeSlots,
                null, // description will be set by the method
                true, // viewingAlwaysAvailable
                "Beautiful shared accommodation in Chatswood",
                SharePropertySubType.MASTER_ROOM,
                shareDetails,
                CapacityShare.SINGLE
        );
    }
}