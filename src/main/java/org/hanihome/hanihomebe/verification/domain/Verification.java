package org.hanihome.hanihomebe.verification.domain;

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
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.member.domain.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    //신원 인증 종류 ex. ID_CARD(신분증), PASSWORD(여권)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationType type;

    //인증 처리 상태 PENDING. APPROVED, REJECTED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus status;

    //사용자가 입력하는 사진 URL
    @ElementCollection
    @CollectionTable(name = "verification_images", joinColumns = @JoinColumn(name = "verification_id"))
    @Column(name = "image_url", length = 1000)
    private List<String> documentImageUrls = new ArrayList<>();

    //반려 사유
    @Column(length = 500)
    private String rejectionReason;


    @Column(nullable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;

    @PrePersist
    protected void onCreate() {
        this.requestedAt = LocalDateTime.now();
    }

    //관리자가 승인할 때 사용
    public void approve() {
        this.status = VerificationStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
    }

    //관리자가 거절할 때 사용
    public void reject(String reason) {
        this.status = VerificationStatus.REJECTED;
        this.rejectionReason = reason;
        this.rejectedAt = LocalDateTime.now();
    }

    public static Verification createRequestFrom(Member member, VerificationType type, List<String> documentImageUrls) {
        return Verification.builder()
                .member(member)
                .type(type)
                .status(VerificationStatus.PENDING)
                .documentImageUrls(documentImageUrls)
                .build();
    }






}
