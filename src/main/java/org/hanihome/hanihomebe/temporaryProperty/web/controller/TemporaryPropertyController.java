package org.hanihome.hanihomebe.temporaryProperty.web.controller;


import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.temporaryProperty.application.TemporaryPropertyService;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.TemporaryPropertyStepSaveRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/temporary-properties")
@RequiredArgsConstructor
public class TemporaryPropertyController {

    private final TemporaryPropertyService temporaryPropertyService;


    @PostMapping
    public ResponseEntity<Void> createTemporaryProperty(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody TemporaryPropertyStepSaveRequestDTO dto) {
        Long hostId = userDetails.getUserId();
        temporaryPropertyService.temporaryPropertyCheckAndSave(hostId, dto, null);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{temporaryPropertyId}")
    public ResponseEntity<Void> updateTemporaryProperty(
            @PathVariable Long temporaryPropertyId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody TemporaryPropertyStepSaveRequestDTO dto
    ) {
        Long hostId = userDetails.getUserId();
        temporaryPropertyService.temporaryPropertyCheckAndSave(hostId, dto, temporaryPropertyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
