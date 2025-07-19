package org.hanihome.hanihomebe.property.application.converter;


import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.response.basic.PropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.basic.RentPropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.basic.SharePropertyResponseDTO;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PropertyDefaultConverter implements PropertyConverter<PropertyResponseDTO> {
    @Override
    public PropertyViewType supports() {
        return PropertyViewType.DEFAULT;
    }

    @Override
    public PropertyResponseDTO convert(PropertyConvertContext propertyConvertContext) {
        return toResponseDTO(propertyConvertContext.getProperty(), propertyConvertContext.getOptionItems());
    }


    PropertyResponseDTO toResponseDTO(Property entity, List<OptionItemResponseDTO> optionItems) {
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

    private <T> T safeCast(Object entity, Class<T> targetClass) {
        if (entity instanceof HibernateProxy) {
            return targetClass.cast(Hibernate.unproxy(entity));
        }
        return targetClass.cast(entity);

    }
}
