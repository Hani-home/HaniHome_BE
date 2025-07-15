package org.hanihome.hanihomebe.property.application.converter;

import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;

public interface PropertyConverter<T> {
    PropertyViewType supports();

    T convert(PropertyConvertContext propertyConvertContext);

}