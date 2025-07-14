package org.hanihome.hanihomebe.property.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.command.PropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.enums.*;
import org.hanihome.hanihomebe.property.domain.item.PropertyOptionItem;
import org.hanihome.hanihomebe.property.domain.vo.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

//TODO: 엔티티들 제약조건 모두 작성필요
//  DTO도 검증 제약조건 작성 필요
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "properties")
@Entity
public abstract class Property {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "property_id")
    private Long id;
    // TODO: 시간관리 클래스인 BaseEntity 별개로 사용되다보니 유지보수에 혼란이 있어보임. BaseEntity를 interface로 두고 implements를 여러개 생성하는 방식으로라도 관리가 필요할듯
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    /**
     * 소유자
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * 매물 상태
     */
    // 노출 상태: 노출, 숨김
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private DisplayStatus displayStatus = DisplayStatus.ACTIVE;
    // 거래 상태: 거래 가능, 거래 중, 거래 완료
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus = TradeStatus.BEFORE;

    /**
     * 1. 매물 종류: SHARE / RENT
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PropertySuperType kind;


    /**
     * 4. 선호 성별
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GenderPreference genderPreference;

    private boolean lgbtAvailable;
    /**
     * 5. 주소
     */
    @Embedded
    private Region region;

    /**
     * 6. 매물 사진 (URL 리스트)
     */
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "property_photos",
            joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "photo_urls", nullable = false)
    private List<String> photoUrls = new ArrayList<>();

    //TODO: 썸네일 생성 및 경로 설정 생각이 필요할듯
    private String thumbnailUrl;

    /**
     * 8. 거래 비용
     */
    @Embedded
    private CostDetails costDetails;

    // 8-2. 옵션 아이템: 수도세, 전기세 ...
    // 해당 엔티티는 독립적인 CRUD가 없을거라고 예상되므로 양방향+cascade해도 무방해보임
    @Builder.Default
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PropertyOptionItem> optionItems = new ArrayList<>();

    /**
     * 10. 거주 조건
     */

    @Embedded
    private LivingConditions livingConditions;

    /**
     * 11. 입주 가능일 (시간 단위)
     */
    @Embedded
    private MoveInInfo moveInInfo;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ParkingOption parkingOption;


    /**
     * 15-1 뷰잉 가능 날짜
     */
    private LocalDate meetingDateFrom;
    private LocalDate meetingDateTo;

    /**
     * 15-2 뷰잉 가능 시간
     * 최소 1개, 최대 3개(아침, 점심, 저녁)
     */
    @ElementCollection
    @CollectionTable(name = "property_time_slot",
            joinColumns = @JoinColumn(name = "property_id"))
    @AttributeOverrides({   // 컬렉션 테이블의 칼럼 개수가 2개이므로 값 설정
            @AttributeOverride(
                    name = "timeFrom",
                    column = @Column(name = "time_from", nullable = false)
            ),
            @AttributeOverride(
                    name = "timeTo",
                    column = @Column(name = "time_to", nullable = false)
            )
    })
    private List<TimeSlot> timeSlots = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "property_viewing_available_date_time",
        joinColumns = @JoinColumn(name = "property_id"))
    @AttributeOverrides({
            @AttributeOverride(
                    name = "date",
                    column = @Column(name = "date", nullable = false)
            ),
            @AttributeOverride(
                    name = "time",
                    column = @Column(name = "time", nullable = false)
            ),
            @AttributeOverride(
                    name = "isReserved",
                    column = @Column(name = "is_reserved", nullable = false)
            ),
            @AttributeOverride(
                    name = "timeInterval",
                    column = @Column(name = "time_interval", nullable = false)
            )
    })
    private List<ViewingAvailableDateTime> viewingAvailableDateTimes = new ArrayList<>(); // 00:00 ~ 24:00, 단위: 30분

    boolean viewingAlwaysAvailable;

    /**
     * 16. 매물 소개
     */
    private String description;

    /**
     * 17. 찜한 수
     */
    @Column(name = "wish_count", nullable = false)
    private int wishCount = 0;

    // 찜한 수 up => 찜하기 추가 서비스 로직에서 사용
    public void incrementWishCount() {
        this.wishCount++;
    }

    //찜한 수 down => 찜하기 삭제 서비스 로직에서 사용 예정
    public void decrementWishCount() {
        if (this.wishCount > 0) {
            this.wishCount--;
        }
    }


    /**
     * 공통 DTO 변환 메서드
     */
    public void addPropertyOptionItem(PropertyOptionItem propertyOptionItem) {
        optionItems.add(propertyOptionItem);
        propertyOptionItem.setProperty(this);
    }

    protected void updateBase(PropertyPatchCommand cmd) {
        if (cmd.getGenderPreference() != null) {
            this.genderPreference = cmd.getGenderPreference();
        }
        if (cmd.getLgbtAvailable() != null) {
            this.lgbtAvailable = cmd.getLgbtAvailable();
        }
        if (cmd.getRegion() != null) {
            this.region = cmd.getRegion();
        }
        if (cmd.getPhotoUrls() != null) {
            this.photoUrls = cmd.getPhotoUrls();
        }
        if (cmd.getCostDetails() != null) {
            this.costDetails = cmd.getCostDetails();
        }
        if (cmd.getOptionItems() != null) {
            this.optionItems.clear();
            this.optionItems.addAll(cmd.getOptionItems());
        }
        if (cmd.getLivingConditions() != null) {
            this.livingConditions = cmd.getLivingConditions();
        }
        if (cmd.getMoveInInfo() != null) {
            this.moveInInfo = cmd.getMoveInInfo();
        }
        if (cmd.getParkingOption() != null) {
            this.parkingOption = cmd.getParkingOption();
        }
        if (cmd.getMeetingDateFrom() != null) {
            this.meetingDateFrom = cmd.getMeetingDateFrom();
        }
        if (cmd.getMeetingDateTo() != null) {
            this.meetingDateTo = cmd.getMeetingDateTo();
        }
        if (cmd.getTimeSlots() != null) {
            this.timeSlots = cmd.getTimeSlots();
        }
        if (cmd.getViewingAlwaysAvailable() != null) {
            this.viewingAlwaysAvailable = cmd.getViewingAlwaysAvailable();
        }
        if (cmd.getDescription() != null) {
            this.description = cmd.getDescription();
        }
        if (cmd.getDisplayStatus() != null) {
            this.displayStatus = cmd.getDisplayStatus();
        }
        if (cmd.getTradeStatus() != null) {
            this.tradeStatus = cmd.getTradeStatus();
        }
        if (cmd.getViewingAlwaysAvailable() != null) {
            this.viewingAlwaysAvailable = cmd.getViewingAlwaysAvailable();
        }
    }


    public abstract Property update(PropertyPatchCommand cmd);
}
