package org.hanihome.hanihomebe.viewing.domain;
import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.global.BaseEntity;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO: guest, host 모두 viewing에서 볼 수 있도록 해야할듯
@Table(name = "viewings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA용 기본 생성자
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Entity
public class Viewing extends BaseEntity {

    @Column(name = "viewing_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 뷰잉 요청자(게스트) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /** 예약된 매물(부동산) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    /** 예약 일시 (meetingDay) */
    @Column(name = "meeting_day", nullable = false)
    private LocalDateTime meetingDay;

    /** 예약 상태 (예: PENDING, CONFIRMED, CANCELLED) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ViewingStatus status;

    private String cancelReason;
//    private List<Long> allOptionItemIds;
    /** 매물 노트 */
    // 1. 매물 사진
    @Builder.Default
    @Column(name = "photo_urls", nullable = false, columnDefinition = "TEXT")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "viewing_photos", joinColumns = @JoinColumn(name = "viewing_id"))
    private List<String> photoUrls = new ArrayList<>();

    // 2. 메모
    @Column(length = 500)
    private String memo;


    /** 체크리스트 및 취소 사유*/
    @OneToMany(mappedBy = "viewing",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<ViewingOptionItem> viewingOptionItems = new HashSet<>();


    /**
     * 새로운 Viewing을 생성하는 팩토리 메서드
     */
    public static Viewing create(Member member, Property property, LocalDateTime meetingDay) {
        return Viewing.builder()
                .member(member)
                .property(property)
                .meetingDay(meetingDay)
                .status(ViewingStatus.REQUESTED)
                .build();
    }

    /** 뷰잉 취소 */
    public void cancel(String cancelReason, List<ViewingOptionItem> cancelReasonItems) {
        this.status = ViewingStatus.CANCELLED;
        this.cancelReason = cancelReason;
        updateViewingOptionItem(cancelReasonItems);
    }

    /**
     * 체크리스트 업로드/업데이트
     */
    public void updateChecklist(List<ViewingOptionItem> allOptionItems) {
        updateViewingOptionItem(allOptionItems);
    }

    public void complete() {
        this.status = ViewingStatus.COMPLETED;
    }

    // 연관관계 편의 메서드
    // TODO: 현재는 옵션아이템을 하나씩 넣어주고 있음. but, 서로 다른 카테고리의 옵션 아이템이 동시에 존재하므로 수정이 어려움
    //  => OptionItem을 변경하는 모든 요청은 해당 엔티티가 소유한 모든 옵션아이템의 식별자를 제공하고, 그것으로 PUT하는 거로 결정(PUT 동작)
/*    public void addViewingOptionItem(ViewingOptionItem viewingOptionItem) {
        viewingOptionItem.setViewing(this);
        this.viewingOptionItems.add(viewingOptionItem);
    }*/

    public void updateViewingOptionItem(List<ViewingOptionItem> viewingOptionItems) {
        this.viewingOptionItems.clear();
        this.viewingOptionItems.addAll(viewingOptionItems);

        viewingOptionItems.forEach(item -> item.setViewing(this));
    }
    public void updateNote(List<String> fileUrls, String memo) {
//        this.photoUrls = List.copyOf(fileUrls);   JPA에서는 불변리스트를 사용하면 문제생김?
        this.photoUrls.clear();
        this.photoUrls.addAll(fileUrls);
        this.memo = memo;
    }
}

