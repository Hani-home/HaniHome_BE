package org.hanihome.hanihomebe.temporaryProperty.web.controller;


import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.temporaryProperty.application.TemporaryPropertyService;
import org.hanihome.hanihomebe.temporaryProperty.web.dto.create.TemporaryPropertyCreateRequestDTO;
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

@RestController
@RequestMapping("/api/v1/temporary-properties")
@RequiredArgsConstructor
public class TemporaryPropertyController {

    private final TemporaryPropertyService temporaryPropertyService;


    @PostMapping
    public ResponseEntity<Void> createTemporaryProperty(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody TemporaryPropertyCreateRequestDTO dto) {
        Long hostId = userDetails.getUserId();
        temporaryPropertyService.createTemporaryProperty(hostId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
