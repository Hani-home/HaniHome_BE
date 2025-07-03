package org.hanihome.hanihomebe.wishlist.repository;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.wishlist.domain.WishItem;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishItemRepository extends JpaRepository<WishItem, Long> {
    boolean existsByMemberAndTargetTypeAndTargetId(Member member, WishTargetType targetType, Long targetId);
    List<WishItem> findByMemberAndTargetType(Member member, WishTargetType targetType);
    List<WishItem> findByMember(Member member);
    Optional<WishItem> findByMemberAndTargetTypeAndTargetId(Member member, WishTargetType targetType, Long targetId);
    void deleteAllByTargetTypeAndTargetId(WishTargetType targetType, Long targetId);
    long countByTargetTypeAndTargetId(WishTargetType targetType, Long targetId);



}

