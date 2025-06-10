/*
package org.hanihome.hanihomebe.interest.region;

import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.member.domain.Member;

@Entity
@Table(name = "interest_regions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "region_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class InterestRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    */
/** 관심 지역을 등록한 사용자 *//*

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    */
/** 사용자가 관심을 표시한 지역 *//*

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;


    */
/**
     * 팩토리 메서드: 새로운 관심 지역 매핑 생성
     *//*

    public static InterestRegion create(Member member, Region region) {
        return InterestRegion.builder()
                .member(member)
                .region(region)
                .build();
    }
}

*/
