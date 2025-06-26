package org.hanihome.hanihomebe.viewing.web.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.notification.application.service.*;
import org.hanihome.hanihomebe.notification.web.dto.NotificationCreateDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.viewing.application.service.ViewingNotificationService;
import org.hanihome.hanihomebe.viewing.application.service.ViewingService;
import org.hanihome.hanihomebe.viewing.web.dto.*;
import org.hanihome.hanihomebe.viewing.web.dto.request.ViewingCancelRequestDTO;
import org.hanihome.hanihomebe.viewing.web.dto.request.ViewingCreateDTO;
import org.hanihome.hanihomebe.viewing.web.dto.response.ViewingResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 사용자별 뷰잉 조회
    @GetMapping("/my-viewings")
    public ResponseEntity<List<ViewingResponseDTO>> getUserViewings(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ViewingResponseDTO> viewings = viewingService.getUserViewings(userDetails.getUserId());
        return ResponseEntity.ok(viewings);
    }

    // 뷰잉 상세조회
    @GetMapping("/{viewingId}")
    public ViewingResponseDTO getViewingById(@PathVariable Long viewingId) {
        return viewingService.getViewingById(viewingId);
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
