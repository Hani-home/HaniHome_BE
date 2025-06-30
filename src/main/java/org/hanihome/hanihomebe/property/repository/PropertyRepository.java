package org.hanihome.hanihomebe.property.repository;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.enums.DisplayStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>, PropertySearchRepository {
    /**
     * 특정 회원이 가진 매물을 노출상태에 따라 조회
     * @param member 매물 호스트
     * @param displayStatus 노출 상태
     * @return
     */
    @Query("select p from Property p where p.member=:member and (:displayStatus is null or :displayStatus = p.displayStatus)")
    List<Property> findByMemberAndDisplayStatus(Member member, DisplayStatus displayStatus);
}
