package org.hanihome.hanihomebe.property.application.converter;


import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.member.web.dto.MemberSummaryDTO;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyWithMemberResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.RentPropertyWithMemberResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.SharePropertyWithMemberResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.basic.RentPropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.basic.SharePropertyResponseDTO;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PropertyDefaultConverter implements PropertyConverter<PropertyWithMemberResponseDTO> {
    @Override
    public PropertyViewType supports() {
        return PropertyViewType.DEFAULT;
    }

    @Override
    public PropertyWithMemberResponseDTO convert(PropertyConvertContext propertyConvertContext) {
        return toResponseDTO(propertyConvertContext.getProperty(), propertyConvertContext);
    }


    PropertyWithMemberResponseDTO toResponseDTO(Property entity, PropertyConvertContext context) {
        List<OptionItemResponseDTO> optionItems = context.getOptionItems();
        MemberSummaryDTO memberSummary = context.getHostSummaryDTO();
        PropertySuperType propertyType = entity.getKind();
        switch (propertyType) {
            case SHARE:
                return SharePropertyWithMemberResponseDTO.from(SharePropertyResponseDTO.from(safeCast(entity, ShareProperty.class), optionItems),
                        memberSummary,
                        context.getMetaInfo());
            case RENT:
                return RentPropertyWithMemberResponseDTO.from(RentPropertyResponseDTO.from(safeCast(entity, RentProperty.class), optionItems),
                        memberSummary,
                        context.getMetaInfo());
            default:
                throw new CustomException(ServiceCode.INVALID_PROPERTY_TYPE);
        }

    }

    private <T> T safeCast(Object entity, Class<T> targetClass) {
        if (entity instanceof HibernateProxy) {
            return targetClass.cast(Hibernate.unproxy(entity));
        }
        return targetClass.cast(entity);
    }
}
