package org.hanihome.hanihomebe.report.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.report.service.ReportService;
import org.hanihome.hanihomebe.report.web.dto.ReportRequestDTO;
import org.hanihome.hanihomebe.report.web.dto.ReportResponseDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> createReport(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ReportRequestDTO dto) {

        log.info(">>> [ReportController] createReport 호출됨");
        log.info("userDetails: {}", userDetails);


        Long reporterId = userDetails.getUserId();

        reportService.createReport(reporterId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
