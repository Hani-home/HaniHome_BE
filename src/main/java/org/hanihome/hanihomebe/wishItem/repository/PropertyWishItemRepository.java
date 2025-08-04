package org.hanihome.hanihomebe.wishItem.repository;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.wishItem.domain.PropertyWishItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyWishItemRepository extends JpaRepository<PropertyWishItem, Long> {
    List<PropertyWishItem> findByMember(Member member);
    boolean existsByMemberAndProperty(Member member, Property property);
    void deleteByMemberAndProperty(Member member, Property property);
}
