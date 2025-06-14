package org.hanihome.hanihomebe.viewing.web.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.viewing.application.service.ViewingService;
import org.hanihome.hanihomebe.viewing.web.dto.request.ViewingCancelDTO;
import org.hanihome.hanihomebe.viewing.web.dto.request.ViewingCreateDTO;
import org.hanihome.hanihomebe.viewing.web.dto.response.ViewingResponseDTO;
import org.springframework.http.HttpStatus;
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
    public void cancelViewing(@RequestBody @Validated ViewingCancelDTO dto) {
        viewingService.cancelViewing(dto);
    }
}
