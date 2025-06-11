package org.hanihome.hanihomebe.viewing.application.service;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.repository.ViewingRepository;
import org.hanihome.hanihomebe.viewing.web.dto.ViewingCreateDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.hanihome.hanihomebe.viewing.domain.ViewingTimeInterval.MINUTE30;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ViewingService {
    
    private final ViewingRepository viewingRepository;
    private final MemberRepository memberRepository;
    private final PropertyRepository propertyRepository;

    /**
     * 뷰잉 생성
     * 1. 최소 1개에서 최대 3개의 시각 선택
     * 2. 자신의 뷰잉 스케줄 확인
     * 3. 시간대 중복 체크
     * 4. 가능한 시간대 중 가장 빠른 시간대로 확정
     */
    @Transactional
    public Long createViewing(ViewingCreateDTO dto) {
        // 0. Member, Property 조회
        Member findMember = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));
        Property findProperty = propertyRepository.findById(dto.getPropertyId())
                .orElseThrow(() -> new CustomException(ServiceCode.PROPERTY_NOT_EXISTS));

        // 1. 선택된 시간 개수 검증
        List<LocalDateTime> preferredTimes = dto.getPreferredTimes();
        if (preferredTimes.isEmpty() || preferredTimes.size() > 3) {
            throw new CustomException(ServiceCode.VIEWING_NUMBER_NOT_SATISFIED);
        }
        
        // 2. 사용자의 기존 뷰잉 스케줄 조회
        List<Viewing> existingViewings = viewingRepository.findByMemberAndMeetingDayAfter(findMember, LocalDateTime.now());
        
        // 3. 시간대 중복 체크
        List<LocalDateTime> availableTimes = preferredTimes.stream()
            .filter(preferredTime -> !isTimeConflict(preferredTime, existingViewings))
            .toList();
        
        if (availableTimes.isEmpty()) {
            throw new CustomException(ServiceCode.VIEWING_ALREADY_PRESCHEDULED);
        }
        
        // 4. 가능한 시간대 중 가장 빠른 시간대로 확정
        LocalDateTime confirmedTime = availableTimes.stream()
            .min(Comparator.naturalOrder())
            .orElseThrow(() -> new IllegalStateException("적절한 시간을 찾을 수 없습니다."));
        
        // 5. 뷰잉 생성 및 저장
        Viewing viewing = Viewing.create(findMember, findProperty, confirmedTime);
        
        return viewingRepository.save(viewing).getId();
    }

    /**
     * 사용자별 뷰잉 조회
     */
    public List<ViewingResponse> getUserViewings(Long memberId) {
        return viewingRepository.findByMemberId(memberId).stream()
            .map(viewing -> ViewingResponse.builder()
                .id(viewing.getId())
                .propertyId(viewing.getPropertyId())
                .viewingDate(viewing.getViewingDate())
                .status(viewing.getStatus())
                .build())
            .toList();
    }

    /**
     * 뷰잉 취소
     */
    @Transactional
    public void cancelViewing(Long viewingId) {
        Viewing viewing = viewingRepository.findById(viewingId)
            .orElseThrow(()->new CustomException(ServiceCode.VIEWING_NOT_EXISTS));

        viewing.cancel();
        viewingRepository.save(viewing);
    }
    
    /**
     * 시간 중복 체크
     * 각 뷰잉 시간으로부터 30분 동안은 새로운 뷰잉을 잡을 수 없음
     */
    private boolean isTimeConflict(LocalDateTime newTime, List<Viewing> existingViewings) {
        return existingViewings.stream()
            .anyMatch(viewing -> {
                LocalDateTime existingTimeStart = viewing.getMeetingDay();
                LocalDateTime existingTimeEnd = existingTimeStart.plusMinutes(MINUTE30.getNumber());
            
            // newTime이 기존 뷰잉 시간의 30분 구간 내에 있는지 확인
            return !(newTime.isBefore(existingTimeStart) || newTime.isAfter(existingTimeEnd));
        });
}
}