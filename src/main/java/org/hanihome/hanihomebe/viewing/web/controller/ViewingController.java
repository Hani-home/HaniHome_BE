package org.hanihome.hanihomebe.viewing.web.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
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

    // create
    @PostMapping
    public ViewingResponseDTO createViewing(@RequestBody @Validated ViewingCreateDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return viewingService.createViewing(dto, userDetails.getUserId());
    }

    //read

    // 사용자별 뷰잉 조회
    @GetMapping("/members/me")
    public ResponseEntity<List<ViewingResponseDTO>> getUserViewings(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ViewingResponseDTO> viewings = viewingService.getUserViewings(userDetails.getUserId());
        return ResponseEntity.ok(viewings);
    }

    // cancel
    @PostMapping("/cancel")
    public void cancelViewing(@RequestBody @Validated ViewingCancelRequestDTO dto) {
        viewingService.cancelViewing(dto);
    }

    // 취소 이유 데이터 조회
    @GetMapping("/{viewingId}/cancel")
    public ViewingCancelResponseDTO getCancelInfo(@PathVariable Long viewingId) {
        return viewingService.getCancelInfo(viewingId);
    }

    // 뷰잉 - 매물 노트 업로드
    @PostMapping("/property-notes")
    public ViewingNotesResponseDTO uploadNotes(@RequestBody ViewingNotesRequestDTO dto) {
        return viewingService.uploadViewingNotes(dto);
    }

    // 체크리스트 - 아이템 선택
    @PostMapping("/checklists")
    public ViewingChecklistResponseDTO uploadChecklist(@RequestBody ViewingChecklistRequestDTO dto) {
        return viewingService.uploadChecklist(dto);
    }

    // 체크리스트 - 선택된 아이템 조회
    @GetMapping("/{viewingId}/checklists")
    public ViewingChecklistResponseDTO getViewingChecklist(@PathVariable Long viewingId) {
        return viewingService.getViewingChecklist(viewingId);
    }

}
