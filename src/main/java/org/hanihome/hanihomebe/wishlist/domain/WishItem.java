package org.hanihome.hanihomebe.wishlist.domain;


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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.global.BaseEntity;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;

@Entity
@Table(
        name = "wish_item",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_member_target",
                        columnNames = {"member_id", "target_type", "target_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //단지 JPA만을 위해
public class WishItem extends BaseEntity {//lastModifiedAt은 현재는 쓸 일이 없을 것 같긴 하지만... 혹시모를 확장성, 그리고 일관성을 위해

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WishTargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    /*
    메모 남길 수 있는 Description 있어도 좋을 듯...?
     */

    public static WishItem createFrom(Member member, WishTargetType targetType, Long targetId) {
        return new WishItem(member, targetType, targetId);
    }

    private WishItem(Member member, WishTargetType targetType, Long targetId) {
        this.member = member;
        this.targetType = targetType;
        this.targetId = targetId;
    }




}
