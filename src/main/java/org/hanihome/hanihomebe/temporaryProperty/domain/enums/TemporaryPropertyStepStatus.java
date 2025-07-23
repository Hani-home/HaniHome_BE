package org.hanihome.hanihomebe.temporaryProperty.domain.enums;

import java.util.List;

public enum TemporaryPropertyStepStatus {
    ADDRESS_AND_PHOTOS("주소와 사진"),
    DETAIL("매물 상세"),
    CONDITION("입주 조건"),
    CONTRACT("계약 사항"),
    COMPLETED("작성 완료");

    private final String name;

    TemporaryPropertyStepStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<TemporaryPropertyStepStatus> getStepsUpTo(TemporaryPropertyStepStatus currentStep) {
        return List.of(values()).subList(0, currentStep.ordinal() + 1); //이거 확인
    }
}
