package org.hanihome.hanihomebe.report.web;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.report.service.ReportService;
import org.hanihome.hanihomebe.report.web.dto.ReportRequestDTO;
import org.hanihome.hanihomebe.report.web.dto.ReportResponseDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports/")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> createReport(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ReportRequestDTO dto) {
        Long reporterId = userDetails.getUserId();

        reportService.createReport(reporterId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
