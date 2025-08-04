package org.hanihome.hanihomebe.wishItem;

import io.lettuce.core.dynamic.annotation.Param;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface PropertyWishItemRepository extends JpaRepository<PropertyWishItem, Long> {
    List<PropertyWishItem> findByMember(Member member);
    boolean existsByMemberAndProperty(Member member, Property property);
    void deleteByMemberAndProperty(Member member, Property property);
}
