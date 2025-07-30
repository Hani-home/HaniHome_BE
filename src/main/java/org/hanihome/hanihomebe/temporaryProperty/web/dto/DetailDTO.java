
package org.hanihome.hanihomebe.temporaryProperty.web.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,       // type 정보를 문자열 NAME으로 사용
        include = JsonTypeInfo.As.PROPERTY, // JSON에 property로 추가
        property = "type"                 // JSON 안에서 "type" 필드를 보고 결정
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RentDetailDTO.class, name = "RENT"),
        @JsonSubTypes.Type(value = ShareDetailDTO.class, name = "SHARE")
})
public interface DetailDTO {
    //subtype(매물 유형)은 구현체에서 받아야 함
    //capacity도
    //매물 정보도(내부 면적 요런거)
    //부동산 중계 여부는 렌트만
    List<Long> highlightOptionItemIds(); //매물의 장점
    List<Long> optionItemIds(); //기본 제공 친구들
}
