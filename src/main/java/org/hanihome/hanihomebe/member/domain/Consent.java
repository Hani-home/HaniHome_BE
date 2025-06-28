package org.hanihome.hanihomebe.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.global.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Consent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 동의 항목 종류 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsentType type;

    /** 동의 여부 (true: 동의, false: 거부) */
    @Column(nullable = false)
    private Boolean agreed;


    /** 어떤 회원이 한 동의인지 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /** 정적 팩토리 메서드 (생성 편의용) */
    public static Consent create(Member member, ConsentType type, boolean agreed) {
        return Consent.builder()
                .member(member)
                .type(type)
                .agreed(agreed)
                .build();
    }
}
