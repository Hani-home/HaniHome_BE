package org.hanihome.hanihomebe.property.application;

import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.web.dto.RentPropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.SharePropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.RentPropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.SharePropertyResponseDTO;
import org.mapstruct.*;


@Mapper(componentModel = "spring")  // 구현 클래스 생성해서 스프링빈으로 등록함
public interface PropertyMapper {

    /* ───────────────────── ① Entity ➜ DTO 스냅샷 ───────────────────── */
    @Mapping(target = "photoUrls", ignore = true)   // ★ 지연 로딩 컬렉션은 필요에 따라 제외
    RentPropertyPatchRequestDTO toPatchDTO(RentProperty entity);   //기본적으로 MapStruct가 동일한 필드명을 자동 매핑합니다.
    @Mapping(target = "photoUrls", ignore = true)   // ★ 지연 로딩 컬렉션은 필요에 따라 제외
    SharePropertyPatchRequestDTO toPatchDTO(ShareProperty entity);

    /* ───────────────────── ② DTO ➜ Entity 부분 업데이트 ─────────────── */
    @BeanMapping(ignoreByDefault = true)    // setter를 사용한 자동 맵핑 옵션 끄기
    void applyPatch(RentPropertyPatchRequestDTO dto, @MappingTarget RentProperty entity);
    @BeanMapping(ignoreByDefault = true)
    void applyPatch(SharePropertyPatchRequestDTO dto, @MappingTarget ShareProperty entity);

    /* 실제 update 호출: DTO값을 엔티티에 반영 – setter 사용 없이 도메인 메서드 호출 */
    @AfterMapping
    default void callUpdate(RentPropertyPatchRequestDTO dto, @MappingTarget RentProperty entity) {
        entity.update(dto); }

    @AfterMapping
    default void callUpdate(SharePropertyPatchRequestDTO dto, @MappingTarget ShareProperty entity) {
        entity.update(dto); }

    default PropertyResponseDTO toResponseDto(Property entity) {
        if(entity instanceof ShareProperty) {
            return SharePropertyResponseDTO.from((ShareProperty) entity);
        } else if (entity instanceof RentProperty) {
            return RentPropertyResponseDTO.from((RentProperty) entity);
        } else {
            throw new RuntimeException("Unsupported entity type: " + entity.getClass().getName());
        }
    }
}