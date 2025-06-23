package org.hanihome.hanihomebe.verification.web.converter;

import org.hanihome.hanihomebe.verification.domain.Verification;
import org.hanihome.hanihomebe.verification.web.dto.VerificationResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class VerificationConverter {
    public static VerificationResponseDTO toVerificationResponseDTO(Verification verification) {
        return new VerificationResponseDTO(
                verification.getId(),
                verification.getType(),
                verification.getStatus(),
                verification.getDocumentImageUrls(),
                verification.getRejectionReason(),
                verification.getRequestedAt(),
                verification.getApprovedAt(),
                verification.getRejectedAt()
        );
    }

    public static List<VerificationResponseDTO> toVerificationResponseDTOList(List<Verification> verifications) {
        return verifications.stream()
                .map(VerificationConverter::toVerificationResponseDTO)
                .collect(Collectors.toList());
    }
}
