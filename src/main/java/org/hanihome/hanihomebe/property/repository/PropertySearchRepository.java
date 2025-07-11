package org.hanihome.hanihomebe.property.repository;

import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.property.web.dto.request.PropertySearchConditionDTO;

import java.util.List;

public interface PropertySearchRepository {
    List<Property> search(PropertySearchConditionDTO conditionDTO);
}
