package org.hanihome.hanihomebe.member.web.dto;

import lombok.Getter;
import org.hanihome.hanihomebe.member.domain.ConsentType;

@Getter
public class ConsentAgreementDTO {
    private ConsentType type;
    private boolean agreed;
}
