package org.hanihome.hanihomebe.admin.customerservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hanihome.hanihomebe.admin.customerservice.domain.OneOnOneConsult;
import org.hanihome.hanihomebe.admin.customerservice.domain.OneOnOneConsultStatus;

@AllArgsConstructor
@Builder
@Getter
public class OneOnOneConsultResponseDTO {
    private String content;
    private Long customerId;
    private OneOnOneConsultStatus status;
    private String email;

    public static OneOnOneConsultResponseDTO from(OneOnOneConsult oneOnOneConsult) {
        return OneOnOneConsultResponseDTO.builder()
                .content(oneOnOneConsult.getContent())
                .customerId(oneOnOneConsult.getCustomer().getId())
                .status(oneOnOneConsult.getStatus())
                .email(oneOnOneConsult.getEmail())
                .build();
    }
}
