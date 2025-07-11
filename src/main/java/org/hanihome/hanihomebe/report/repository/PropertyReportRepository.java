package org.hanihome.hanihomebe.report.repository;

import org.hanihome.hanihomebe.report.application.domain.PropertyReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyReportRepository extends JpaRepository<PropertyReport, Long> {
}
