package org.hanihome.hanihomebe.property.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hanihome.hanihomebe.interest.region.Region;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.enums.Capacity;
import org.hanihome.hanihomebe.property.domain.enums.GenderPreference;
import org.hanihome.hanihomebe.property.domain.enums.ParkingOption;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.domain.option.PropertyOptionItem;
import org.hanihome.hanihomebe.property.web.dto.PropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.SharePropertyPatchRequestDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "properties")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Getter
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
public abstract class Property {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "property_id")
    private Long id;

    /** 소유자 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    /**1. 매물 종류: SHARE / RENT */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PropertySuperType kind;

    /** 3. 수용인원 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Capacity capacity;

    /** 4. 선호 성별 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GenderPreference genderPreference;

    /** 5. 주소 */
    @Embedded
    private Region region;

    /**
     * 6. 매물 사진 (URL 리스트)
     */
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "property_photos",
            joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "photo_url", nullable = false)
    private List<String> photoUrls = new ArrayList<>();

    /** 8. 거래 비용 */
    // 8-1. 비용 (주 단위)
    @Column(nullable = false)
    private BigDecimal weeklyCost;

    // 8-2. 포함된 비용항목: 수도세, 전기세 ...
    // 해당 엔티티는 독립적인 CRUD가 없을거라고 예상되므로 양방향+cascade해도 무방해보임
    @Builder.Default
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PropertyOptionItem> optionItems = new ArrayList<>();

    // 8-3. bill 설명
    @Column(columnDefinition = "TEXT")
    private String costDescription;

    // 8-4. 보증금
    private BigDecimal deposit;

    // 8-5. 키 보증금
    private BigDecimal keyDeposit;

    /** 10. 거주 조건 */
    // 10-1. 노티스 (주 단위)
    private Integer noticePeriodWeeks;

    // 10-2. 최소 거주 기간 (주 단위)
    private Integer minimumStayWeeks;

    // 10-3. 계약 형태 설명
    private String contractTerms;

    /**
     * 11. 입주 가능일 (시간 단위)
     */
    @ElementCollection
    @CollectionTable(name = "property_available_from",
            joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "available_from")
    private Set<LocalDateTime> availableFrom = new HashSet<>();

    /** 12. 기타 허용/불가 */
/* OptionItem에서 처리
    private Boolean smokerAllowed;
    private Boolean petAllowed;
    private Boolean visitorAllowed;
    private Boolean kitchenUseAllowed;
*/

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ParkingOption parkingOption;


    /** 13. 기본 제공 가전·가구 */
    /* OptionItem에서 처리
    @OneToMany(mappedBy = "property")
    private Set<PropertyAmenityItem> amenities;
*/
    /** 14. 매물의 장점 */
/* OptionItem에서 처리
    @ElementCollection(targetClass = Feature.class)
    @CollectionTable(name = "property_features",
            joinColumns = @JoinColumn(name = "property_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "feature")
    private Set<Feature> features;
*/

    /**
     * 15. 뷰잉 가능 날짜
     */
    @ElementCollection
    @CollectionTable(name = "property_viewing_dates",
            joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "viewing_date")
    private Set<LocalDateTime> viewingDates = new HashSet<>();

    /** 16. 매물 소개 */
    private String description;



    /** 공통 DTO 변환 메서드 */
    public void addPropertyOptionItem(PropertyOptionItem propertyOptionItem) {
        optionItems.add(propertyOptionItem);
        propertyOptionItem.setProperty(this);
    }

    protected void updateBase(PropertyPatchRequestDTO dto) {
        if (dto.getCapacity() != null) {
            this.capacity = dto.getCapacity();
        }
        if (dto.getGenderPreference() != null) {
            this.genderPreference = dto.getGenderPreference();
        }
        if (dto.getRegion() != null) {
            this.region = dto.getRegion();
        }
        if (dto.getPhotoUrls() != null) {
            this.photoUrls = dto.getPhotoUrls();
        }
        if (dto.getWeeklyCost() != null) {
            this.weeklyCost = dto.getWeeklyCost();
        }
        if (dto.getOptionItems() != null) {
            this.optionItems = dto.getOptionItems();
        }
        if (dto.getCostDescription() != null) {
            this.costDescription = dto.getCostDescription();
        }
        if (dto.getDeposit() != null) {
            this.deposit = dto.getDeposit();
        }
        if (dto.getKeyDeposit() != null) {
            this.keyDeposit = dto.getKeyDeposit();
        }
        if (dto.getNoticePeriodWeeks() != null) {
            this.noticePeriodWeeks = dto.getNoticePeriodWeeks();
        }
        if (dto.getMinimumStayWeeks() != null) {
            this.minimumStayWeeks = dto.getMinimumStayWeeks();
        }
        if (dto.getContractTerms() != null) {
            this.contractTerms = dto.getContractTerms();
        }
        if (dto.getAvailableFrom() != null) {
            this.availableFrom = dto.getAvailableFrom();
        }
        if (dto.getParkingOption() != null) {
            this.parkingOption = dto.getParkingOption();
        }
        if (dto.getViewingDates() != null) {
            this.viewingDates = dto.getViewingDates();
        }
        if (dto.getDescription() != null) {
            this.description = dto.getDescription();
        }
    }


    public abstract Property update(PropertyPatchRequestDTO dto);
}
