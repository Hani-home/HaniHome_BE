package org.hanihome.hanihomebe.temporaryProperty.web.controller;


import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.temporaryProperty.application.TemporaryPropertyService;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryPropertyCreateRequestDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.response.TemporaryPropertyListResponseDTO;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.response.TemporaryPropertyResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/temporary-properties")
@RequiredArgsConstructor
public class TemporaryPropertyController {

    private final TemporaryPropertyService temporaryPropertyService;


    @PostMapping
    public ResponseEntity<TemporaryPropertyListResponseDTO> createTemporaryProperty(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody TemporaryPropertyCreateRequestDTO dto) {
        Long hostId = userDetails.getUserId();

        if (dto.id() == null) {
            TemporaryPropertyListResponseDTO response = temporaryPropertyService.createTemporaryProperty(hostId, dto);
            return ResponseEntity.ok(response);
        } else {
            TemporaryPropertyListResponseDTO response = temporaryPropertyService.updateTemporaryProperty(hostId, dto);
            return ResponseEntity.ok(response);// 200 OK 또는 204 No Content도 가능
        }

    }

    @GetMapping
    public ResponseEntity<List<TemporaryPropertyListResponseDTO>> getTemporaryProperties(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long hostId = userDetails.getUserId();
        List<TemporaryPropertyListResponseDTO> response = temporaryPropertyService.getTemporaryProperties(hostId);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{temporaryPropertyId}")
    public ResponseEntity<TemporaryPropertyResponseDTO> getTemporaryProperty(@AuthenticationPrincipal CustomUserDetails userDetails , @PathVariable Long temporaryPropertyId) {
        Long hostId = userDetails.getUserId();
        TemporaryPropertyResponseDTO response = temporaryPropertyService.getTemporaryProperty(hostId, temporaryPropertyId);
        return ResponseEntity.ok(response);
    }



    //삭제
    @DeleteMapping("/{temporaryPropertyId}")
    public ResponseEntity<Void> deleteTemporaryProperty(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long temporaryPropertyId ) {
        Long hostId = userDetails.getUserId();
        temporaryPropertyService.deleteTemporaryProperty(hostId, temporaryPropertyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //조회 summary랑 detail 두개로 갑니다.




}
