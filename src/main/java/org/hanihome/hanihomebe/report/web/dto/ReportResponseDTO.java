package org.hanihome.hanihomebe.report.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hanihome.hanihomebe.report.application.domain.Report;
import org.hanihome.hanihomebe.report.application.domain.ReportTargetType;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReportResponseDTO {
    private Long reportId;
    private ReportTargetType reportTargetType;
    private Long targetId;
    private String description;
    private List<String> documentImageUrls;

    public static ReportResponseDTO from(ReportTargetType targetType, Long targetId, Report report) {
        return new ReportResponseDTO(
                report.getId(),
                targetType,
                targetId,
                report.getDescription(),
                report.getImageUrls()
        );
    }
}
