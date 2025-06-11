package org.hanihome.hanihomebe.viewing.repository;

import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface ViewingRepository extends JpaRepository<Viewing, Long> {
    List<Viewing> findByMemberAndMeetingDayAfter(Member member, LocalDateTime meetingDayAfter);

    Collection<Object> findByMemberId(Long memberId);
}
