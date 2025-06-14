package org.hanihome.hanihomebe.viewing.domain;
import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.global.BaseEntity;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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

    /** 상태 변경 로직 등 도메인 메서드도 이곳에 추가 가능 */
    public void cancel(String cancelReason) {
        this.status = ViewingStatus.CANCELLED;
        this.cancelReason = cancelReason;
    }

    public void complete() {
        this.status = ViewingStatus.COMPLETED;
    }
}

