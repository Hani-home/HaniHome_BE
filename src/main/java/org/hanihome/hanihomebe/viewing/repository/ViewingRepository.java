package org.hanihome.hanihomebe.viewing.repository;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.domain.ViewingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface ViewingRepository extends JpaRepository<Viewing, Long>, CustomViewingRepository{
    /**
     * 특정 사용자의 예정되어있는 뷰잉 조회
     * @param member :뷰잉 당사자
     * @param meetingDayAfter :기준 시각(보통 LocalDateTime.now())
     * @param status :뷰잉 상태(보통 ViewingStatus.REQUESTED)
     * @return
     */
    List<Viewing> findByMemberAndMeetingDayAfterAndStatus(Member member, LocalDateTime meetingDayAfter, ViewingStatus status);

    List<Viewing> findByProperty_IdAndStatus(Long propertyId, ViewingStatus status);

    List<Viewing> findByProperty_Id(Long propertyId);

    @Query("select v from Viewing v " +
            "where v.member.id = :memberId " +
            "and (:status is null or :status = v.status)")
    List<Viewing> findViewingsAsGuestAndStatus(@Param("memberId") Long memberId,
                                               @Param("status") ViewingStatus status);

    @Query("select v from Viewing  v " +
            "where v.property.member.id = :memberId " +
            "and (:status is null or :status = v.status)")
    List<Viewing> findViewingsAsHostAndStatus(Long memberId, ViewingStatus status);

    void deleteByProperty_Id(Long propertyId);
}
