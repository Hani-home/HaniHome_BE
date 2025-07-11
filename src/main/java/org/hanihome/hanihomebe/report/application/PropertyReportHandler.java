package org.hanihome.hanihomebe.report.application;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.repository.PropertyRepository;
import org.hanihome.hanihomebe.report.application.domain.PropertyReport;
import org.hanihome.hanihomebe.report.application.domain.Report;
import org.hanihome.hanihomebe.report.application.domain.ReportTargetType;
import org.hanihome.hanihomebe.report.repository.PropertyReportRepository;
import org.hanihome.hanihomebe.report.web.dto.ReportRequestDTO;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PropertyReportHandler implements ReportHandler {
    
    private final PropertyRepository propertyRepository;
    private final PropertyReportRepository propertyReportRepository;

    @Override
    public ReportTargetType getTargetType() {
        return ReportTargetType.PROPERTY;
    }

    @Override
    public Report createAndSave(Member reporter, ReportRequestDTO dto) {
        if (!ReportTargetType.PROPERTY.equals(dto.getTargetType())) {
            throw new CustomException(ServiceCode.PROPERTY_REPORT);
        }

        Property property = propertyRepository.findById(dto.getTargetId())
                .orElseThrow(() -> new CustomException(ServiceCode.PROPERTY_NOT_EXISTS));

        PropertyReport propertyReport = PropertyReport.create(reporter, property, dto.getDescription(), dto.getDocumentImageUrls());
        return propertyReportRepository.save(propertyReport);
    }

    @Override
    public boolean validate(ReportRequestDTO dto) {
        return propertyRepository.existsById(dto.getTargetId());
    }
}
