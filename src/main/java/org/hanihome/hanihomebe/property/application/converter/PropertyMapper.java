package org.hanihome.hanihomebe.property.application.converter;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.domain.command.RentPropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.command.SharePropertyPatchCommand;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.web.dto.request.RentPropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.request.SharePropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.RentPropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.SharePropertyResponseDTO;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.mapstruct.*;

import java.util.List;


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
    default void callUpdate(RentPropertyPatchCommand cmd, @MappingTarget RentProperty entity) {
        entity.update(cmd); }

    @AfterMapping
    default void callUpdate(SharePropertyPatchCommand cmd, @MappingTarget ShareProperty entity) {
        entity.update(cmd); }


    default PropertyResponseDTO toResponseDTO(Property entity, List<OptionItemResponseDTO> optionItems) {
        PropertySuperType propertyType = entity.getKind();
        switch (propertyType) {
            case SHARE:
                return SharePropertyResponseDTO.from(safeCast(entity, ShareProperty.class), optionItems);
            case RENT:
                return RentPropertyResponseDTO.from(safeCast(entity, RentProperty.class), optionItems);
            default:
                throw new CustomException(ServiceCode.INVALID_PROPERTY_TYPE);
        }
    }

    /*default PropertySummaryDTO toSummaryDTO(Property entity) {
        PropertySuperType propertyType = entity.getKind();
        switch (propertyType) {
            case SHARE:
                return SharePropertySummaryDTO.from(safeCast(entity, ShareProperty.class));
            case RENT:
                return RentPropertySummaryDTO.from(safeCast(entity, RentProperty.class));
            default:
                throw new CustomException(ServiceCode.INVALID_PROPERTY_TYPE);
        }
    }*/
    private <T> T safeCast(Object entity, Class<T> targetClass) {
        if (entity instanceof HibernateProxy) {
            return targetClass.cast(Hibernate.unproxy(entity));
        }
        return targetClass.cast(entity);

    }
}