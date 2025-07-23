
package org.hanihome.hanihomebe.temporaryProperty.web.dto;

import java.util.List;

public interface DetailDTO {
    //subtype(매물 유형)은 구현체에서 받아야 함
    //capacity도
    //매물 정보도(내부 면적 요런거)
    //부동산 중계 여부는 렌트만
    List<Long> highlightOptionItemIds(); //매물의 장점
    List<Long> optionItemIds(); //기본 제공 친구들
}
