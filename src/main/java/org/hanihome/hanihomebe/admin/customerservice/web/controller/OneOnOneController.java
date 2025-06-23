package org.hanihome.hanihomebe.admin.customerservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.admin.customerservice.application.OneOnOneService;
import org.hanihome.hanihomebe.admin.customerservice.domain.OneOnOneConsultStatus;
import org.hanihome.hanihomebe.admin.customerservice.web.dto.OneOnOneConsultCreateDTO;
import org.hanihome.hanihomebe.admin.customerservice.web.dto.OneOnOneConsultResponseDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/one-on-one-consult")
@RestController
public class OneOnOneController {
    private final OneOnOneService oneOnOneService;

    // 상담 등록
    @PostMapping
    public OneOnOneConsultResponseDTO create(@RequestBody OneOnOneConsultCreateDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return oneOnOneService.createOneOnOneConsult(dto, userDetails.getUserId());
    }

    // 모든 일대일 상담 내역 조회
    @GetMapping
    public List<OneOnOneConsultResponseDTO> getAll(@RequestParam(name = "status", required = false) OneOnOneConsultStatus status) {
        if (status != null) {
            return oneOnOneService.getAllByStatus(status);
        } else {
            return oneOnOneService.getAllOneOnOneConsult();
        }
    }

    // 운영자는 문의에 이메일로 답변 완료
    @PostMapping("/{oneOnOneConsultId}/reply")
    public void replyByEmail(@PathVariable Long oneOnOneConsultId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        oneOnOneService.replyByEmail(oneOnOneConsultId, userDetails.getUserId());
    }
}
