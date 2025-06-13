package org.hanihome.hanihomebe.viewing.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.member.repository.MemberRepository;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.domain.ViewingOptionItem;
import org.hanihome.hanihomebe.viewing.domain.ViewingStatus;
import org.hanihome.hanihomebe.viewing.repository.ViewingRepository;
import org.hanihome.hanihomebe.viewing.web.dto.*;
import org.hanihome.hanihomebe.viewing.web.dto.request.ViewingCancelRequestDTO;
import org.hanihome.hanihomebe.viewing.web.dto.request.ViewingCreateDTO;
import org.hanihome.hanihomebe.viewing.web.dto.response.ViewingResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.hanihome.hanihomebe.viewing.domain.ViewingTimeInterval.MINUTE30;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ViewingService {
    
    private final ViewingRepository viewingRepository;
    private final MemberRepository memberRepository;
    private final PropertyRepository propertyRepository;
    private final OptionItemRepository optionItemRepository;
    private final OptionCategoryRepository optionCategoryRepository;

    /**
     * 뷰잉 생성
     * 1. 최소 1개에서 최대 3개의 시각 선택
     * 2. 자신의 뷰잉 스케줄 확인
     * 3. 시간대 중복 체크
     * 4. 가능한 시간대 중 가장 빠른 시간대로 확정
     */
    @Transactional
    public ViewingResponseDTO createViewing(ViewingCreateDTO dto, Long memberId) {
        // 0. Member, Property 조회
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ServiceCode.MEMBER_NOT_EXISTS));
        Property findProperty = propertyRepository.findById(dto.getPropertyId())
                .orElseThrow(() -> new CustomException(ServiceCode.PROPERTY_NOT_EXISTS));
        // 1. 선택된 시간 개수 검증
        List<LocalDateTime> preferredTimes = dto.getPreferredTimes();
        if (preferredTimes.isEmpty() || preferredTimes.size() > 3) {
            throw new CustomException(ServiceCode.VIEWING_NUMBER_NOT_SATISFIED);
        }
        
        // 2. 사용자의 기존 다가오는 뷰잉 스케줄 조회
        List<Viewing> existingViewings = viewingRepository.findByMemberAndMeetingDayAfterAndStatus(findMember, LocalDateTime.now(), ViewingStatus.REQUESTED);

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
        viewingRepository.save(viewing);

        return ViewingResponseDTO.from(viewing);
    }

    /**
     * 사용자별 뷰잉 조회
     */
    public List<ViewingResponseDTO> getUserViewings(Long memberId) {
        return viewingRepository.findByMemberId(memberId).stream()
                .map(viewing -> ViewingResponseDTO.from(viewing))
                .toList();
    }

    /**
     * 뷰잉 취소
     */
    @Transactional
    public void cancelViewing(ViewingCancelRequestDTO dto) {
        Viewing viewing = viewingRepository.findById(dto.getViewingId())
            .orElseThrow(()->new CustomException(ServiceCode.VIEWING_NOT_EXISTS));

        List<ViewingOptionItem> viewingOptionItems = optionItemRepository.findAllById(dto.getOptionItemIds())
                .stream()
                .map(ViewingOptionItem::create)
                .toList();

        viewing.cancel(dto.getReason(), viewingOptionItems);

        viewingRepository.save(viewing);
    }

    public ViewingCancelResponseDTO getCancelInfo(Long viewingId) {
        Viewing findViewing = viewingRepository.findById(viewingId)
                .orElseThrow(()->new CustomException(ServiceCode.VIEWING_NOT_EXISTS));

        List<Long> cancelReasonItemIds = getSelectedOptionItemIdsInCategory(findViewing, CategoryCode.VIEWING_CAT1);
        log.info("cancelReasonItemIds: {}", cancelReasonItemIds);
        return ViewingCancelResponseDTO.from(viewingId, cancelReasonItemIds, findViewing.getCancelReason());
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



    /**
     * 매물 노트 수정
     */
    @Transactional
    public ViewingNotesResponseDTO uploadViewingNotes(ViewingNotesRequestDTO dto) {
        Viewing findViewing = viewingRepository.findById(dto.viewingId())
            .orElseThrow(()->new CustomException(ServiceCode.VIEWING_NOT_EXISTS));

        findViewing.updateNote(dto.fileUrls(), dto.memo());
        viewingRepository.save(findViewing);

        return ViewingNotesResponseDTO.from(findViewing);
    }



    /**
     * 뷰잉 체크리스트에서 선택된 아이템 조회하기
     *
     * @param viewingId
     * @return : Viewing 식별자, 체크리스트에서 선택된 OptionItem 식별자
     */
    public ViewingChecklistResponseDTO getViewingChecklist(Long viewingId) {
        Viewing findViewing = viewingRepository.findById(viewingId)
            .orElseThrow(()->new CustomException(ServiceCode.VIEWING_NOT_EXISTS));

        // VIEWING_CAT2(체크리스트 카테고리)
        List<Long> checklistItemIds = getSelectedOptionItemIdsInCategory(findViewing, CategoryCode.VIEWING_CAT2);

        return ViewingChecklistResponseDTO.from(viewingId, checklistItemIds);

    }



    /**
     * 뷰잉 체크리스트에 사용자가 체크한 항목을 저장합니다
     * @param dto: 사용자가 체크한 아이템 식별자
     * @return : 체크리스트에서 사용자가 체크한 아이템 식별자
     */
    @Transactional
    public ViewingChecklistResponseDTO uploadChecklist(ViewingChecklistRequestDTO dto) {
        Viewing findViewing = viewingRepository.findById(dto.viewingId())
                .orElseThrow(() -> new CustomException(ServiceCode.VIEWING_NOT_EXISTS));

        List<OptionItem> optionItems = optionItemRepository.findAllById(dto.optionItemIds());

        // ViewingOptionItem 생성(체크리스트 아이템)
        optionItems.forEach(optionItem -> {
            ViewingOptionItem viewingOptionItem = ViewingOptionItem.create(optionItem);
            findViewing.addViewingOptionItem(viewingOptionItem);
        });

        viewingRepository.save(findViewing);

        return ViewingChecklistResponseDTO.from(findViewing.getId(), optionItems.stream().map(OptionItem::getId).toList());
    }

    /**
     * Viewing에서 특정 CategoryCode에 해당하는 ViewingOptionItem을 찾고 OptionItem.id를 반환한다
     *
     * @param viewing
     * @param categoryCode :찾고 카테고리
     * @return
     */
    private List<Long> getSelectedOptionItemIdsInCategory(Viewing viewing, CategoryCode categoryCode) {
        OptionCategory category = optionCategoryRepository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new CustomException(ServiceCode.OPTION_CATEGORY_NOT_INITIALIZED));

        List<Long> checklistItemIds = viewing.getViewingOptionItems()
                .stream()
                .filter(viewingOptionItem -> viewingOptionItem.getOptionItem().getOptionCategory().equals(category))
                .map(viewingOptionItem -> viewingOptionItem.getOptionItem().getId())
                .toList();
        return checklistItemIds;
    }
}