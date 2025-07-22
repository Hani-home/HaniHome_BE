package org.hanihome.hanihomebe.property.application.converter;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.metro.web.dto.nearest.NearestMetroStopResponseDTO;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.domain.RentProperty;
import org.hanihome.hanihomebe.property.domain.ShareProperty;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.response.summary.MetaInfo;
import org.hanihome.hanihomebe.property.web.dto.response.summary.PropertySummaryDTO;
import org.hanihome.hanihomebe.property.web.dto.response.summary.RentPropertySummaryDTO;
import org.hanihome.hanihomebe.property.web.dto.response.summary.SharePropertySummaryDTO;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Component;

@Component
public class PropertySummaryConverter implements PropertyConverter<PropertySummaryDTO>{
    @Override
    public PropertyViewType supports() {
        return PropertyViewType.SUMMARY;
    }

    @Override
    public PropertySummaryDTO convert(PropertyConvertContext propertyConvertContext) {
        return toSummaryDTO(propertyConvertContext.getProperty(),
                propertyConvertContext.getNearestMetroStopResponseDTO(),
                propertyConvertContext.getMetaInfo()
        );
    }

    PropertySummaryDTO toSummaryDTO(Property entity,
                                    NearestMetroStopResponseDTO nearestMetroStopResponseDTO,
                                    MetaInfo metaInfo) {
        PropertySuperType propertyType = entity.getKind();
        switch (propertyType) {
            case SHARE:
                return SharePropertySummaryDTO.from(safeCast(entity, ShareProperty.class),
                        nearestMetroStopResponseDTO,
                        metaInfo);
            case RENT:
                return RentPropertySummaryDTO.from(safeCast(entity, RentProperty.class),
                        nearestMetroStopResponseDTO,
                        metaInfo);
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
