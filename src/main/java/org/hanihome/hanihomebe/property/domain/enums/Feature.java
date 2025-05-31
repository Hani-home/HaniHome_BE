package org.hanihome.hanihomebe.property.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
/** TODO: 해당 값을 엔티티로 가져가도 괜찮음
 * 이유: 해당 enum은 변경(추가/수정) 가능성이 높음
 * */

@Getter
@RequiredArgsConstructor
public enum Feature {
    GOOD_SUNLIGHT("햇빛이 잘 들어요"),
    NEAR_AMENITIES("주변 편의 시설이 많아요"),
    GOOD_VIEW("전망이 좋아요"),
    CHEAPER_THAN_NEIGHBOR("주변 보다 저렴해요"),
    TERRACE("테라스가 있어요"),
    CONVENIENT_TRANSPORT("교통이 편리해요"),
    GOOD_COMMUNITY("커뮤니티 시설이 좋아요"),
    CLEAN("집 상태가 깨끗해요"),
    SOUNDPROOF("방음이 잘 돼요"),
    SAFE_NEIGHBORHOOD("치안이 좋아요");

    private final String name;
}
