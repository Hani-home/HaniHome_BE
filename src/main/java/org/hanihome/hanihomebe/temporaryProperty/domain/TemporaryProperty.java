package org.hanihome.hanihomebe.temporaryProperty.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.vo.TimeSlot;
import org.hanihome.hanihomebe.property.domain.vo.ViewingAvailableDateTime;
import org.hanihome.hanihomebe.temporaryProperty.domain.item.TemporaryPropertyOptionItem;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryCostDetails;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryLivingConditions;
import org.hanihome.hanihomebe.temporaryProperty.domain.vo.TemporaryMoveInInfo;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.response.TemporaryPropertyResponseDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) //부모와 자식 클래스가 각각 테이블을 만들고 PK로 조인함. 자식들이 부모를 FK로 가지고 있음
@Table(name = "temporary_property")
@SuperBuilder//상속할 때 부모 + 자식 필드 모두를 포함한 빌더 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)//JPA 엔티티 이벤트 감지해서 자동으로 값 넣어줌 @CreatedDate, @LastModifiedDate
public abstract class TemporaryProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temporary_property_id")
    private Long id;


    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @Enumerated(EnumType.STRING)
    private TemporaryPropertyStepStatus status;
    /*
    1. 이넘작성
    2. 도메인 연결
    3. DTO 수정
    4. 정적 메서드 팩토리 수정
    5. 서비스 수정
     */


    //3개만 가질 수 있도록 이건 Authprincipal로 가져오기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    //젤 처음에 결정
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    protected PropertySuperType kind;

    //저장 상태: 주소와 사진, 매물 상세, 입주 조건, 계약 사항
    //진행 현황에 맞게 데이터가 잘 왔는지 확인도 해야할 듯.... 로직이 복잡해질 것 같네

    //3. 입주조건
    @Enumerated(EnumType.STRING)
    protected GenderPreference genderPreference;

    //3. 입주 조건
    protected Boolean lgbtAvailable;

    //1. 주소와 사진
    @Embedded
    private Region region;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "temporary_property_photos",
            joinColumns = @JoinColumn(name = "temporary_property_id"))
    @Column(name = "photo_urls") //저장 단계니 null도 이론상 가능
    private List<String> photoUrls = new ArrayList<>();

    //4. 계약 사항(비용, 빌포함 여부, 빌 설명, 디파짓, Key 디파짓) 빌에 포함된 항목은 없음.
    @Embedded
    private TemporaryCostDetails costDetails;

    /*
    2. 매물 상세: 이 매물의 장점은 무엇인가요?, 기본 제공 가전과 가구,
    3. 입주 조건: 다음 항목이 가능한가?,
    4. 계약 사항: 빌에 포함된 내역
     */
    @Builder.Default
    @OneToMany(mappedBy = "temporaryProperty", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    //One 쪽이 주인이 아님 => OptionItems에서 FK 칼럼 소유
    private List<TemporaryPropertyOptionItem> optionItems = new ArrayList<>();

    //3. 입주 조건 (노티스, 최소 거주 기간, 계약 형태 설명, 계약 연장 가능 여부)
    @Embedded
    private TemporaryLivingConditions livingConditions;

    //3. 입주 조건 (언제부터 언제까지, 즉시 입주 가능, 입주 일자 협의 가능)
    @Embedded
    private TemporaryMoveInInfo moveInInfo;

    //4. 계약사항. 뷰잉 가능 날짜
    private LocalDate meetingDateFrom;
    private LocalDate meetingDateTo;

    //4. 계약 사항:  뷰잉 가능 시간대
    @ElementCollection
    @CollectionTable(name = "temporary_property_time_slots",
            joinColumns = @JoinColumn(name = "temporary_property_id"))
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


    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "temporary_property_viewing_available_date_times",
            joinColumns = @JoinColumn(name = "temporary_property_id"))
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
    private List<ViewingAvailableDateTime> viewingAvailableDateTimes = new ArrayList<>();


    //4. 계약 사항: 뷰잉 항상 가능 여부 boolean
    Boolean viewingAlwaysAvailable;

    //이건 어떤 단계에 넣어야 할까?
    private String description;


    public void addTemporaryPropertyOptionItem(TemporaryPropertyOptionItem temporaryPropertyOptionItem) {
        optionItems.add(temporaryPropertyOptionItem);
        temporaryPropertyOptionItem.setTemporaryProperty(this);
    }

    public void updateBase(TemporaryPropertyCreateRequestDTO dto) {
        this.kind = dto.kind();
        this.status = dto.status();
        this.genderPreference = dto.genderPreference();
        this.lgbtAvailable = dto.lgbtAvailable();
        this.region = dto.region();
        this.photoUrls = dto.photoUrls();
        this.costDetails = dto.costDetails() != null
                ? dto.costDetails().toTemporaryVO()
                : TemporaryCostDetails.empty();
        this.livingConditions = dto.livingConditions() != null
                ? dto.livingConditions().toTemporaryVO()
                : TemporaryLivingConditions.empty();
        this.moveInInfo = dto.moveInInfo() != null
                ? dto.moveInInfo().toTemporaryVO()
                : TemporaryMoveInInfo.empty();
        this.meetingDateFrom = dto.meetingDateFrom();
        this.meetingDateTo = dto.meetingDateTo();
        this.timeSlots = dto.timeSlots();
        this.viewingAvailableDateTimes = dto.viewingAvailableDateTimes();
        this.viewingAlwaysAvailable = dto.viewingAlwaysAvailable();
        this.description = dto.description();
    }

    public abstract TemporaryProperty update(TemporaryPropertyCreateRequestDTO temporaryPropertyCreateRequestDTO);

    public void clearTemporaryPropertyOptionItems() {
        this.optionItems.clear();
    }

    public abstract TemporaryPropertyResponseDTO toResponseDTO();



}
