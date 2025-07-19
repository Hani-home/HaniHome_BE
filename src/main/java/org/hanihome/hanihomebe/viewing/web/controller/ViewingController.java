package org.hanihome.hanihomebe.viewing.web.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.application.service.*;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.viewing.application.service.ViewingNotificationService;
import org.hanihome.hanihomebe.viewing.application.service.ViewingService;
import org.hanihome.hanihomebe.viewing.domain.ViewingStatus;
import org.hanihome.hanihomebe.viewing.web.dto.ViewingDTOByView;
import org.hanihome.hanihomebe.viewing.web.dto.ViewingResponseDTO;
import org.hanihome.hanihomebe.viewing.web.dto.cancel.ViewingCancelRequestDTO;
import org.hanihome.hanihomebe.viewing.web.dto.cancel.ViewingCancelResponseDTO;
import org.hanihome.hanihomebe.viewing.web.dto.checklist.ViewingChecklistRequestDTO;
import org.hanihome.hanihomebe.viewing.web.dto.checklist.ViewingChecklistResponseDTO;
import org.hanihome.hanihomebe.viewing.web.dto.note.ViewingNotesResponseDTO;
import org.hanihome.hanihomebe.viewing.web.dto.ViewingCreateDTO;
import org.hanihome.hanihomebe.viewing.web.dto.note.ViewingNotesRequestDTO;
import org.hanihome.hanihomebe.viewing.web.enums.ViewingViewType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/viewings")
@RestController
public class ViewingController {

    private final ViewingService viewingService;
    private final NotificationFacadeService notificationFacadeService;
    private final NotificationMessageFactory notificationMessageFactory;
    private final ViewingNotificationService viewingNotificationService;

    // create
    @PostMapping
    public ViewingResponseDTO createViewing(@RequestBody @Validated ViewingCreateDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 1. viewing 생성
        ViewingResponseDTO responseDTO = viewingService.createViewing(dto, userDetails.getUserId());
        // 2. 호스트에게 알림 전송
        Long viewingId = responseDTO.getId();
        viewingNotificationService.sendViewingCreateNotification(userDetails.getUsername(), viewingId);
        // 3. 뷰잉 리마인더 알림 스케줄링
        viewingNotificationService.sendReminderNotification(viewingId, responseDTO.getMeetingDay());
        return responseDTO;
    }

    //read

    // 내 뷰잉 조회
    @GetMapping("/my-viewings")
    public ResponseEntity<List<? extends ViewingDTOByView>> getUserViewings(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                            @RequestParam(required = false) ViewingViewType view) {
        List<? extends ViewingDTOByView> viewings = viewingService.getViewingByMemberId(userDetails.getUserId(), view);
        return ResponseEntity.ok(viewings);
    }

    // 뷰잉 상세조회
    @GetMapping("/{viewingId}")
    public ViewingResponseDTO getViewingById(@PathVariable Long viewingId) {
        return viewingService.getViewingById(viewingId);
    }

    // REQUESTED 상태인 내 뷰잉 시각 조회
    @GetMapping("/my-viewings/dates")
    public Map<LocalDate, List<LocalTime>> getMyViewingDates(@RequestParam ViewingStatus status,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        return viewingService.getMyViewingDatesByStatus(userDetails.getUserId(), status);

    }

    // cancel
    @PutMapping("/{viewingId}/cancel")
    public void cancelViewing(@RequestBody @Validated ViewingCancelRequestDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // viewing 취소
        viewingService.cancelViewing(dto);
        // 알림 생성 및 전송
        viewingNotificationService.sendViewingCanceledNotification(userDetails.getUserId(), dto.getViewingId());
    }

    // 취소 이유 데이터 조회
    @GetMapping("/{viewingId}/cancel")
    public ViewingCancelResponseDTO getCancelInfo(@PathVariable Long viewingId) {
        return viewingService.getCancelInfo(viewingId);
    }

    // 매물 노트 업로드
    @PutMapping("/{viewingId}/property-notes")
    public ViewingNotesResponseDTO uploadNotes(@RequestBody ViewingNotesRequestDTO dto) {
        return viewingService.uploadViewingNotes(dto);
    }

    // 체크리스트 - 아이템 선택
    @PutMapping("/checklists")
    public ViewingChecklistResponseDTO uploadChecklist(@RequestBody ViewingChecklistRequestDTO dto) {
        return viewingService.uploadChecklist(dto);
    }

    // 체크리스트 - 선택된 아이템 조회
    @GetMapping("/{viewingId}/checklists")
    public ViewingChecklistResponseDTO getViewingChecklist(@PathVariable Long viewingId) {
        return viewingService.getViewingChecklist(viewingId);
    }

}
