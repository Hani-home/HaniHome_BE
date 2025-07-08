package org.hanihome.hanihomebe.report.application.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class PropertyReport extends Report{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "property_id")
    private Property property;

    @Builder//상속일 때는 클래스 말고 메서드에 붙여야 함. 부모 자식 필드를 한 번에 처리 못하기 때문!
    public PropertyReport(Member reporter, Property property, String description, List<String> imageUrls ) {
        super(reporter, description, imageUrls);
        this.property = property;
    }

    public static PropertyReport create(Member reporter, Property property, String description, List<String> imageUrls) {
        return PropertyReport.builder()
                .reporter(reporter)
                .property(property)
                .description(description)
                .imageUrls(imageUrls)
                .build();
    }

    @Override
    public ReportTargetType getTargetType() {
        return ReportTargetType.PROPERTY;
    }

    @Override
    public Long getTargetId() {
        return property.getId();
    }
}
