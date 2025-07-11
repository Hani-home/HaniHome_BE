package org.hanihome.hanihomebe.report.application.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.global.BaseEntity;
import org.hanihome.hanihomebe.member.domain.Member;

import java.util.ArrayList;
import java.util.List;



@NoArgsConstructor
@Getter
@AllArgsConstructor
@MappedSuperclass //이 추상화 클래스는 테이블로 만들지 않을 것
public abstract class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //신고자가 삭제되었을 때 entity도 삭제되면 안될 것 같아 nullable true로 하고 DB에서 On delete set null로 처리하겠습니다(어노테이션은 딱히 없는 것 같네용).
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "reporter_id", nullable = true)
    private Member reporter; //신고자

    /*
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportTargetType targetType;


    @Column(name = "target_id", nullable = false)
    private long targetId;
     */

    @Column(name = "description", nullable = false, length = 600)
    private String description;


    @ElementCollection
    @CollectionTable(name = "report_image_urls", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    public Report(Member reporter, String description, List<String> imageUrls) {
        this.reporter = reporter;
        this.description = description;
        this.imageUrls = (imageUrls != null) ? imageUrls : new ArrayList<>();
    }

    public abstract ReportTargetType getTargetType();
    public abstract Long getTargetId();

    /*
    //정적 메서드
    public static Report createReport(Member reporter, Long targetId, ReportTargetType targetType, String description, List<String> imageUrls) {
        return Report.builder()
                .reporter(reporter)
                .targetId(targetId)
                .targetType(targetType)
                .description(description)
                .imageUrls(imageUrls)
                .build();
    }
     */
}


