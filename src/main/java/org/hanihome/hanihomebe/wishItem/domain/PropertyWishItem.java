package org.hanihome.hanihomebe.wishItem.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;

@Entity
@Table(name = "property_wish", uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "property_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PropertyWishItem extends WishItem {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    private PropertyWishItem(Member member,Property property) {
        this.member = member;
        this.property = property;
    }

    public static PropertyWishItem create(Member member, Property property) {
        return new PropertyWishItem(member, property);

    }
}
